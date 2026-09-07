(async function () {
  const user = await initShell({ title: 'Admin Dashboard', active: 'admin-dashboard', requireAdmin: true });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-admin-dashboard').content.cloneNode(true));

  try {
    const stats = await api.get('/api/admin/stats');
    document.getElementById('statTotalUsers').textContent = stats.totalUsers;
    document.getElementById('statTotalAdmins').textContent = stats.totalAdmins;
    document.getElementById('statTotalSkills').textContent = stats.totalSkills;
    document.getElementById('statTotalRoles').textContent = stats.totalCareerRoles;

    const popularBody = document.getElementById('popularSkillsBody');
    if (!stats.mostPopularSkills.length) {
      popularBody.innerHTML = '<p class="sw-text-muted mb-0" style="font-size:13.5px;">No skill data yet.</p>';
    } else {
      const max = Math.max(...stats.mostPopularSkills.map((s) => s.userCount));
      popularBody.innerHTML = stats.mostPopularSkills.map((s) => `
        <div class="d-flex align-items-center gap-3 mb-3">
          <div style="width: 140px; font-size: 13.5px; color: var(--sw-slate-700);" class="sw-truncate">${escapeHtml(s.skillName)}</div>
          <div class="sw-progress flex-grow-1"><div class="sw-progress-bar" style="width:${(s.userCount / max) * 100}%;"></div></div>
          <div style="width: 24px; font-size: 12.5px;" class="text-end sw-text-muted">${s.userCount}</div>
        </div>`).join('');
    }

    const roleBody = document.getElementById('usersByRoleBody');
    if (!stats.usersByTargetRole.length) {
      roleBody.innerHTML = '<p class="sw-text-muted mb-0" style="font-size:13.5px;">No users have set a career goal yet.</p>';
    } else {
      const max = Math.max(...stats.usersByTargetRole.map((s) => s.userCount));
      roleBody.innerHTML = stats.usersByTargetRole.map((s) => `
        <div class="d-flex align-items-center gap-3 mb-3">
          <div style="width: 180px; font-size: 13.5px; color: var(--sw-slate-700);" class="sw-truncate">${escapeHtml(s.roleName)}</div>
          <div class="sw-progress flex-grow-1"><div class="sw-progress-bar" style="width:${(s.userCount / max) * 100}%;"></div></div>
          <div style="width: 24px; font-size: 12.5px;" class="text-end sw-text-muted">${s.userCount}</div>
        </div>`).join('');
    }
  } catch (err) {
    toast.error(err.message || 'Could not load admin stats');
  }
})();
