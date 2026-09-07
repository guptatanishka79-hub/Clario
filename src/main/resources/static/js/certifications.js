(async function () {
  const user = await initShell({ title: 'Certifications', active: 'certifications' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-certs').content.cloneNode(true));

  const modal = new bootstrap.Modal(document.getElementById('certModal'));
  const form = document.getElementById('certForm');
  let certs = [];

  async function load() {
    certs = await api.get('/api/certifications');
    render();
  }

  function render() {
    const grid = document.getElementById('certsGrid');
    const empty = document.getElementById('certsEmpty');
    if (!certs.length) {
      grid.innerHTML = '';
      empty.classList.remove('d-none');
      return;
    }
    empty.classList.add('d-none');
    grid.innerHTML = certs.map((c) => `
      <div class="col-md-6 col-xl-4">
        <div class="sw-card h-100">
          <div class="sw-card-body">
            <div class="d-flex justify-content-between align-items-start mb-2">
              <div class="sw-stat-icon sw-icon-warning" style="width:38px;height:38px;font-size:15px;"><i class="fa-solid fa-certificate"></i></div>
              ${c.expired ? '<span class="sw-badge sw-badge-error"><span class="sw-badge-dot"></span>Expired</span>' : '<span class="sw-badge sw-badge-success"><span class="sw-badge-dot"></span>Active</span>'}
            </div>
            <h6 class="mb-1">${escapeHtml(c.name)}</h6>
            <div class="sw-text-muted mb-2" style="font-size: 13px;">${escapeHtml(c.issuingOrganization || 'Unknown issuer')}</div>
            <div class="sw-text-muted" style="font-size: 12.5px;">
              ${c.issueDate ? 'Issued ' + formatDate(c.issueDate) : ''}${c.expiryDate ? ' · Expires ' + formatDate(c.expiryDate) : ''}
            </div>
            ${c.credentialId ? `<div class="sw-text-muted mt-1" style="font-size: 12px;">ID: ${escapeHtml(c.credentialId)}</div>` : ''}
            <div class="d-flex justify-content-between align-items-center mt-3">
              ${c.credentialUrl ? `<a href="${escapeHtml(c.credentialUrl)}" target="_blank" rel="noopener" style="font-size:12.5px;"><i class="fa-solid fa-arrow-up-right-from-square me-1"></i>View credential</a>` : '<span></span>'}
              <div>
                <button class="btn btn-light btn-sm me-1" onclick="window.__editCert(${c.id})"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-light btn-sm text-danger" onclick="window.__deleteCert(${c.id}, '${escapeHtml(c.name).replace(/'/g, "\\'")}')"><i class="fa-solid fa-trash"></i></button>
              </div>
            </div>
          </div>
        </div>
      </div>`).join('');
  }

  window.__editCert = (id) => {
    const c = certs.find((x) => x.id === id);
    if (!c) return;
    document.getElementById('certModalTitle').textContent = 'Edit Certification';
    document.getElementById('certId').value = c.id;
    document.getElementById('certName').value = c.name;
    document.getElementById('certOrg').value = c.issuingOrganization || '';
    document.getElementById('certIssueDate').value = c.issueDate || '';
    document.getElementById('certExpiryDate').value = c.expiryDate || '';
    document.getElementById('certCredentialId').value = c.credentialId || '';
    document.getElementById('certCredentialUrl').value = c.credentialUrl || '';
    modal.show();
  };

  window.__deleteCert = async (id, name) => {
    if (!confirm(`Delete "${name}"?`)) return;
    try {
      await api.del('/api/certifications/' + id);
      toast.success('Certification removed');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  document.getElementById('addCertBtn').addEventListener('click', () => {
    form.reset();
    document.getElementById('certModalTitle').textContent = 'Add Certification';
    document.getElementById('certId').value = '';
    modal.show();
  });

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('certId').value;
    const payload = {
      name: document.getElementById('certName').value.trim(),
      issuingOrganization: document.getElementById('certOrg').value.trim() || null,
      issueDate: document.getElementById('certIssueDate').value || null,
      expiryDate: document.getElementById('certExpiryDate').value || null,
      credentialId: document.getElementById('certCredentialId').value.trim() || null,
      credentialUrl: document.getElementById('certCredentialUrl').value.trim() || null,
    };
    try {
      if (id) {
        await api.put('/api/certifications/' + id, payload);
        toast.success('Certification updated');
      } else {
        await api.post('/api/certifications', payload);
        toast.success('Certification added');
      }
      modal.hide();
      load();
    } catch (err) {
      toast.error(err.message);
    }
  });

  await load();
})();
