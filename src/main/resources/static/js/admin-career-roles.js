(async function () {
  const user = await initShell({ title: 'Career Roles', active: 'admin-career-roles', requireAdmin: true });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-admin-roles').content.cloneNode(true));

  const roleModal = new bootstrap.Modal(document.getElementById('roleModal'));
  const requiredSkillsModal = new bootstrap.Modal(document.getElementById('requiredSkillsModal'));
  const roleForm = document.getElementById('roleForm');
  const addRequiredSkillForm = document.getElementById('addRequiredSkillForm');

  let roles = [];
  let allSkills = [];
  let activeRoleId = null;

  async function load() {
    [roles, allSkills] = await Promise.all([
      api.get('/api/admin/career-roles'),
      api.get('/api/admin/skills'),
    ]);
    render();
  }

  function render() {
    const grid = document.getElementById('rolesGrid');
    grid.innerHTML = roles.map((r) => `
      <div class="col-md-6 col-xl-4">
        <div class="sw-card h-100">
          <div class="sw-card-body">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div class="sw-stat-icon sw-icon-indigo" style="width:38px;height:38px;font-size:15px;"><i class="fa-solid fa-briefcase"></i></div>
              <div>
                <button class="btn btn-light btn-sm me-1" onclick="window.__editRole(${r.id})"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-light btn-sm text-danger" onclick="window.__deleteRole(${r.id}, '${escapeHtml(r.name).replace(/'/g, "\\'")}')"><i class="fa-solid fa-trash"></i></button>
              </div>
            </div>
            <h6 class="mb-1">${escapeHtml(r.name)}</h6>
            <p class="sw-text-muted mb-3" style="font-size: 13px; min-height: 40px;">${escapeHtml(r.description || 'No description provided.')}</p>
            <div class="d-flex flex-wrap gap-1 mb-3">
              ${r.requiredSkills.slice(0, 4).map((s) => `<span class="sw-badge sw-badge-neutral">${escapeHtml(s.skillName)}</span>`).join('') || '<span class="sw-text-muted" style="font-size:12.5px;">No required skills yet</span>'}
              ${r.requiredSkills.length > 4 ? `<span class="sw-badge sw-badge-neutral">+${r.requiredSkills.length - 4} more</span>` : ''}
            </div>
            <button class="btn btn-outline-primary btn-sm w-100" onclick="window.__manageSkills(${r.id})">Manage Required Skills</button>
          </div>
        </div>
      </div>`).join('');
  }

  window.__editRole = (id) => {
    const r = roles.find((x) => x.id === id);
    if (!r) return;
    document.getElementById('roleModalTitle').textContent = 'Edit Career Role';
    document.getElementById('roleId').value = r.id;
    document.getElementById('roleName').value = r.name;
    document.getElementById('roleDescription').value = r.description || '';
    roleModal.show();
  };

  window.__deleteRole = async (id, name) => {
    if (!confirm(`Delete career role "${name}"? Users targeting this role will lose their goal.`)) return;
    try {
      await api.del('/api/admin/career-roles/' + id);
      toast.success('Career role deleted');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  document.getElementById('addRoleBtn').addEventListener('click', () => {
    roleForm.reset();
    document.getElementById('roleModalTitle').textContent = 'Add Career Role';
    document.getElementById('roleId').value = '';
    roleModal.show();
  });

  roleForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('roleId').value;
    const payload = {
      name: document.getElementById('roleName').value.trim(),
      description: document.getElementById('roleDescription').value.trim() || null,
    };
    try {
      if (id) {
        await api.put('/api/admin/career-roles/' + id, payload);
        toast.success('Career role updated');
      } else {
        await api.post('/api/admin/career-roles', payload);
        toast.success('Career role created');
      }
      roleModal.hide();
      load();
    } catch (err) {
      toast.error(err.message);
    }
  });

  // ---------- Required skills management ----------

  window.__manageSkills = (id) => {
    activeRoleId = id;
    const role = roles.find((r) => r.id === id);
    document.getElementById('requiredSkillsRoleName').textContent = role.name;

    const select = document.getElementById('requiredSkillSelect');
    select.innerHTML = allSkills.map((s) => `<option value="${s.id}">${escapeHtml(s.name)} <span></span></option>`).join('');
    // Re-render plain options (no HTML in <option> text)
    select.innerHTML = allSkills.map((s) => `<option value="${s.id}">${escapeHtml(s.name)} (${escapeHtml(s.categoryName)})</option>`).join('');

    renderRequiredSkills(role);
    requiredSkillsModal.show();
  };

  function renderRequiredSkills(role) {
    const body = document.getElementById('requiredSkillsBody');
    body.innerHTML = role.requiredSkills.length ? role.requiredSkills.map((s) => `
      <tr>
        <td>${escapeHtml(s.skillName)}</td>
        <td><span class="sw-level-pill ${levelPillClass(s.requiredProficiency)}">${titleCase(s.requiredProficiency)}</span></td>
        <td class="text-end">
          <button class="btn btn-light btn-sm text-danger" onclick="window.__removeRequiredSkill(${s.skillId})"><i class="fa-solid fa-trash"></i></button>
        </td>
      </tr>`).join('') : '<tr><td colspan="3" class="sw-text-muted">No required skills yet.</td></tr>';
  }

  addRequiredSkillForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const skillId = parseInt(document.getElementById('requiredSkillSelect').value, 10);
    const requiredProficiency = document.getElementById('requiredSkillLevel').value;
    try {
      const updatedRole = await api.post(`/api/admin/career-roles/${activeRoleId}/required-skills`, { skillId, requiredProficiency });
      const idx = roles.findIndex((r) => r.id === activeRoleId);
      roles[idx] = updatedRole;
      renderRequiredSkills(updatedRole);
      render();
      toast.success('Required skill added');
    } catch (err) {
      toast.error(err.message);
    }
  });

  window.__removeRequiredSkill = async (skillId) => {
    try {
      const updatedRole = await api.del(`/api/admin/career-roles/${activeRoleId}/required-skills/${skillId}`);
      const idx = roles.findIndex((r) => r.id === activeRoleId);
      roles[idx] = updatedRole;
      renderRequiredSkills(updatedRole);
      render();
      toast.success('Required skill removed');
    } catch (err) {
      toast.error(err.message);
    }
  };

  await load();
})();
