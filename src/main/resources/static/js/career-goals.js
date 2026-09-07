(async function () {
  const user = await initShell({ title: 'Career Goals', active: 'career-goals' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-career-goals').content.cloneNode(true));

  const roleModal = new bootstrap.Modal(document.getElementById('roleModal'));
  let selectedRole = null;
  let currentGoal = null;
  let roles = [];

  async function loadCurrentGoal() {
    const card = document.getElementById('currentGoalCard');
    try {
      currentGoal = await api.get('/api/career-roles/goal');
      card.querySelector('.sw-card-body').innerHTML = `
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
          <div>
            <div class="sw-eyebrow">Current Target Role</div>
            <div class="fw-bold" style="font-size: 19px; color: var(--sw-navy-900);">${escapeHtml(currentGoal.name)}</div>
            <p class="sw-text-muted mb-0 mt-1" style="font-size: 13.5px; max-width: 560px;">${escapeHtml(currentGoal.description || '')}</p>
          </div>
          <a href="/pages/skill-gap.html" class="btn btn-primary btn-sm">View Skill Gap Analysis</a>
        </div>`;
    } catch (e) {
      currentGoal = null;
      card.querySelector('.sw-card-body').innerHTML = `
        <div class="d-flex align-items-center gap-3">
          <div class="sw-stat-icon sw-icon-warning"><i class="fa-solid fa-flag"></i></div>
          <div>
            <div class="fw-semibold" style="color: var(--sw-navy-900);">No career goal set</div>
            <div class="sw-text-muted" style="font-size: 13.5px;">Pick a role below to start tracking your readiness.</div>
          </div>
        </div>`;
    }
  }

  async function loadRoles() {
    roles = await api.get('/api/career-roles');
    const grid = document.getElementById('rolesGrid');
    grid.innerHTML = roles.map((r) => `
      <div class="col-md-6 col-xl-4">
        <div class="sw-card h-100 sw-clickable" onclick="window.__openRole(${r.id})" style="transition: box-shadow .15s ease;">
          <div class="sw-card-body">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <h6 class="mb-0">${escapeHtml(r.name)}</h6>
              ${currentGoal && currentGoal.id === r.id ? '<span class="sw-badge sw-badge-indigo"><span class="sw-badge-dot"></span>Current</span>' : ''}
            </div>
            <p class="sw-text-muted mb-3" style="font-size: 13px; min-height: 40px;">${escapeHtml(r.description || 'No description provided.')}</p>
            <div class="d-flex flex-wrap gap-1">
              ${r.requiredSkills.slice(0, 4).map((s) => `<span class="sw-badge sw-badge-neutral">${escapeHtml(s.skillName)}</span>`).join('')}
              ${r.requiredSkills.length > 4 ? `<span class="sw-badge sw-badge-neutral">+${r.requiredSkills.length - 4} more</span>` : ''}
            </div>
          </div>
        </div>
      </div>`).join('');
  }

  window.__openRole = (id) => {
    selectedRole = roles.find((r) => r.id === id);
    if (!selectedRole) return;
    document.getElementById('roleModalTitle').textContent = selectedRole.name;
    document.getElementById('roleModalDescription').textContent = selectedRole.description || '';
    document.getElementById('roleModalSkillsBody').innerHTML = selectedRole.requiredSkills.map((s) => `
      <tr>
        <td>${escapeHtml(s.skillName)}</td>
        <td><span class="sw-level-pill ${levelPillClass(s.requiredProficiency)}">${titleCase(s.requiredProficiency)}</span></td>
      </tr>`).join('') || '<tr><td colspan="2" class="sw-text-muted">No required skills configured for this role yet.</td></tr>';

    const setBtn = document.getElementById('setGoalBtn');
    if (currentGoal && currentGoal.id === selectedRole.id) {
      setBtn.textContent = 'This Is Your Current Goal';
      setBtn.disabled = true;
    } else {
      setBtn.textContent = 'Set as My Career Goal';
      setBtn.disabled = false;
    }
    roleModal.show();
  };

  document.getElementById('setGoalBtn').addEventListener('click', async () => {
    if (!selectedRole) return;
    try {
      await api.put('/api/career-roles/goal', { careerRoleId: selectedRole.id });
      toast.success(`Career goal set to ${selectedRole.name}`);
      roleModal.hide();
      await loadCurrentGoal();
      await loadRoles();
    } catch (err) {
      toast.error(err.message);
    }
  });

  await loadCurrentGoal();
  await loadRoles();
})();
