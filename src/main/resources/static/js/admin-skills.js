(async function () {
  const user = await initShell({ title: 'Skills', active: 'admin-skills', requireAdmin: true });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-admin-skills').content.cloneNode(true));

  const modal = new bootstrap.Modal(document.getElementById('skillModal'));
  const form = document.getElementById('skillForm');
  let skills = [];
  let categories = [];

  async function load() {
    [skills, categories] = await Promise.all([
      api.get('/api/admin/skills'),
      api.get('/api/admin/categories'),
    ]);
    const select = document.getElementById('skillCategory');
    select.innerHTML = categories.map((c) => `<option value="${c.id}">${escapeHtml(c.name)}</option>`).join('');
    render();
  }

  function render() {
    document.getElementById('skillsTableBody').innerHTML = skills.map((s) => `
      <tr>
        <td class="fw-semibold" style="color: var(--sw-navy-900);">${escapeHtml(s.name)}</td>
        <td><span class="sw-badge sw-badge-neutral">${escapeHtml(s.categoryName)}</span></td>
        <td class="text-end">
          <button class="btn btn-light btn-sm me-1" onclick="window.__editSkill(${s.id})"><i class="fa-solid fa-pen"></i></button>
          <button class="btn btn-light btn-sm text-danger" onclick="window.__deleteSkill(${s.id}, '${escapeHtml(s.name).replace(/'/g, "\\'")}')"><i class="fa-solid fa-trash"></i></button>
        </td>
      </tr>`).join('');
  }

  window.__editSkill = (id) => {
    const s = skills.find((x) => x.id === id);
    if (!s) return;
    document.getElementById('skillModalTitle').textContent = 'Edit Skill';
    document.getElementById('skillId').value = s.id;
    document.getElementById('skillName').value = s.name;
    document.getElementById('skillCategory').value = s.categoryId;
    modal.show();
  };

  window.__deleteSkill = async (id, name) => {
    if (!confirm(`Delete skill "${name}"? This will remove it from any user profiles and role requirements that use it.`)) return;
    try {
      await api.del('/api/admin/skills/' + id);
      toast.success('Skill deleted');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  document.getElementById('addSkillBtn').addEventListener('click', () => {
    form.reset();
    document.getElementById('skillModalTitle').textContent = 'Add Skill';
    document.getElementById('skillId').value = '';
    modal.show();
  });

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('skillId').value;
    const payload = { name: document.getElementById('skillName').value.trim(), categoryId: parseInt(document.getElementById('skillCategory').value, 10) };
    try {
      if (id) {
        await api.put('/api/admin/skills/' + id, payload);
        toast.success('Skill updated');
      } else {
        await api.post('/api/admin/skills', payload);
        toast.success('Skill created');
      }
      modal.hide();
      load();
    } catch (err) {
      toast.error(err.message);
    }
  });

  await load();
})();
