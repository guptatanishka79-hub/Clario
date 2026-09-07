(async function () {
  const currentUser = await initShell({ title: 'Users', active: 'admin-users', requireAdmin: true });
  if (!currentUser) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-admin-users').content.cloneNode(true));

  let users = [];

  async function load() {
    users = await api.get('/api/admin/users');
    render();
  }

  function render() {
    document.getElementById('usersTableBody').innerHTML = users.map((u) => `
      <tr>
        <td class="fw-semibold" style="color: var(--sw-navy-900);">${escapeHtml(u.fullName)}</td>
        <td class="sw-text-muted">${escapeHtml(u.email)}</td>
        <td>
          <select class="form-select form-select-sm" style="width:150px;" data-user-id="${u.id}" data-field="role" ${u.id === currentUser.id ? 'disabled' : ''}>
            <option value="ROLE_USER" ${u.role === 'ROLE_USER' ? 'selected' : ''}>User</option>
            <option value="ROLE_ADMIN" ${u.role === 'ROLE_ADMIN' ? 'selected' : ''}>Admin</option>
          </select>
        </td>
        <td>
          <div class="form-check form-switch">
            <input class="form-check-input" type="checkbox" data-user-id="${u.id}" data-field="enabled" ${u.enabled ? 'checked' : ''} ${u.id === currentUser.id ? 'disabled' : ''}>
          </div>
        </td>
        <td class="text-end">
          <button class="btn btn-light btn-sm text-danger" ${u.id === currentUser.id ? 'disabled title="You cannot delete your own account"' : ''} onclick="window.__deleteUser(${u.id}, '${escapeHtml(u.fullName).replace(/'/g, "\\'")}')">
            <i class="fa-solid fa-trash"></i>
          </button>
        </td>
      </tr>`).join('');

    document.querySelectorAll('select[data-field="role"]').forEach((el) => {
      el.addEventListener('change', () => updateUser(el.dataset.userId, { role: el.value }));
    });
    document.querySelectorAll('input[data-field="enabled"]').forEach((el) => {
      el.addEventListener('change', () => updateUser(el.dataset.userId, { enabled: el.checked }));
    });
  }

  async function updateUser(id, payload) {
    try {
      await api.put('/api/admin/users/' + id, payload);
      toast.success('User updated');
    } catch (err) {
      toast.error(err.message);
      load();
    }
  }

  window.__deleteUser = async (id, name) => {
    if (!confirm(`Delete the account for "${name}"? This cannot be undone.`)) return;
    try {
      await api.del('/api/admin/users/' + id);
      toast.success('User deleted');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  await load();
})();
