(async function () {
  const user = await initShell({ title: 'Settings', active: 'settings' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-settings').content.cloneNode(true));

  document.getElementById('settingsName').textContent = user.fullName;
  document.getElementById('settingsEmail').textContent = user.email;
  document.getElementById('settingsRole').textContent = user.role === 'ROLE_ADMIN' ? 'Administrator' : 'Standard Member';
  document.getElementById('settingsStatus').innerHTML = user.enabled
    ? '<span class="sw-badge sw-badge-success"><span class="sw-badge-dot"></span>Active</span>'
    : '<span class="sw-badge sw-badge-error"><span class="sw-badge-dot"></span>Disabled</span>';

  document.getElementById('settingsLogoutBtn').addEventListener('click', async () => {
    try {
      await api.post('/api/auth/logout');
    } catch (e) { /* ignore */ }
    window.location.href = '/index.html';
  });
})();
