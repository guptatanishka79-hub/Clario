(async function () {
  const user = await initShell({ title: 'My Skills', active: 'skills' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-skills').content.cloneNode(true));

  const skillModalEl = document.getElementById('skillModal');
  const skillModal = new bootstrap.Modal(skillModalEl);
  const skillForm = document.getElementById('skillForm');
  const skillNameInput = document.getElementById('skillNameInput');
  const skillCategoryInput = document.getElementById('skillCategoryInput');
  const proficiencyInput = document.getElementById('proficiencyInput');
  const yearsInput = document.getElementById('yearsInput');
  const skillEntryId = document.getElementById('skillEntryId');

  let categories = [];
  let catalog = [];
  let allSkills = [];

  async function loadReferenceData() {
    categories = await api.get('/api/skills/categories');
    catalog = await api.get('/api/skills/catalog');

    const categoryFilter = document.getElementById('categoryFilter');
    categories.forEach((c) => {
      categoryFilter.insertAdjacentHTML('beforeend', `<option value="${c.id}">${escapeHtml(c.name)}</option>`);
      skillCategoryInput.insertAdjacentHTML('beforeend', `<option value="${c.id}">${escapeHtml(c.name)}</option>`);
    });

    const datalist = document.getElementById('skillCatalogList');
    catalog.forEach((s) => datalist.insertAdjacentHTML('beforeend', `<option value="${escapeHtml(s.name)}">`));
  }

  async function loadSkills() {
    const query = document.getElementById('searchInput').value.trim();
    const categoryId = document.getElementById('categoryFilter').value;
    const params = new URLSearchParams();
    if (query) params.set('query', query);
    if (categoryId) params.set('categoryId', categoryId);
    allSkills = await api.get('/api/skills?' + params.toString());
    renderTable();
  }

  function renderTable() {
    const tbody = document.getElementById('skillsTableBody');
    const empty = document.getElementById('skillsEmpty');
    if (!allSkills.length) {
      tbody.innerHTML = '';
      empty.classList.remove('d-none');
      return;
    }
    empty.classList.add('d-none');
    tbody.innerHTML = allSkills.map((s) => `
      <tr>
        <td class="fw-semibold" style="color: var(--sw-navy-900);">${escapeHtml(s.skillName)}</td>
        <td><span class="sw-badge sw-badge-neutral">${escapeHtml(s.categoryName)}</span></td>
        <td><span class="sw-level-pill ${levelPillClass(s.proficiencyLevel)}">${titleCase(s.proficiencyLevel)}</span></td>
        <td>${s.yearsOfExperience != null ? s.yearsOfExperience + ' yrs' : '—'}</td>
        <td class="sw-text-muted">${formatDate(s.lastUpdated)}</td>
        <td class="text-end">
          <button class="btn btn-light btn-sm me-1" onclick="window.__editSkill(${s.id})"><i class="fa-solid fa-pen"></i></button>
          <button class="btn btn-light btn-sm text-danger" onclick="window.__deleteSkill(${s.id}, '${escapeHtml(s.skillName).replace(/'/g, "\\'")}')"><i class="fa-solid fa-trash"></i></button>
        </td>
      </tr>`).join('');
  }

  window.__editSkill = (id) => {
    const skill = allSkills.find((s) => s.id === id);
    if (!skill) return;
    document.getElementById('skillModalTitle').textContent = 'Edit Skill';
    skillEntryId.value = skill.id;
    skillNameInput.value = skill.skillName;
    skillNameInput.disabled = true;
    skillCategoryInput.closest('.mb-3').classList.add('d-none');
    proficiencyInput.value = skill.proficiencyLevel;
    yearsInput.value = skill.yearsOfExperience || '';
    skillModal.show();
  };

  window.__deleteSkill = async (id, name) => {
    if (!confirm(`Remove "${name}" from your skills?`)) return;
    try {
      await api.del('/api/skills/' + id);
      toast.success('Skill removed');
      loadSkills();
    } catch (err) {
      toast.error(err.message);
    }
  };

  document.getElementById('addSkillBtn').addEventListener('click', () => {
    skillForm.reset();
    document.getElementById('skillModalTitle').textContent = 'Add Skill';
    skillEntryId.value = '';
    skillNameInput.disabled = false;
    skillCategoryInput.closest('.mb-3').classList.remove('d-none');
    skillModal.show();
  });

  document.getElementById('searchInput').addEventListener('input', debounce(loadSkills, 300));
  document.getElementById('categoryFilter').addEventListener('change', loadSkills);

  skillForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const isEdit = !!skillEntryId.value;
    const payload = {
      proficiencyLevel: proficiencyInput.value,
      yearsOfExperience: yearsInput.value ? parseFloat(yearsInput.value) : null,
    };
    if (!isEdit) {
      const matched = catalog.find((s) => s.name.toLowerCase() === skillNameInput.value.trim().toLowerCase());
      if (matched) {
        payload.skillId = matched.id;
      } else {
        payload.skillName = skillNameInput.value.trim();
        payload.categoryId = skillCategoryInput.value || null;
        if (!payload.categoryId) {
          toast.error('Please choose a category for this new skill');
          return;
        }
      }
    }

    try {
      if (isEdit) {
        await api.put('/api/skills/' + skillEntryId.value, payload);
        toast.success('Skill updated');
      } else {
        await api.post('/api/skills', payload);
        toast.success('Skill added');
      }
      skillModal.hide();
      await loadReferenceData();
      await loadSkills();
    } catch (err) {
      toast.error(err.message);
    }
  });

  function debounce(fn, ms) {
    let t;
    return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), ms); };
  }

  await loadReferenceData();
  await loadSkills();
})();
