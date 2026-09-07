(async function () {
  const user = await initShell({ title: 'Categories', active: 'admin-categories', requireAdmin: true });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-admin-categories').content.cloneNode(true));

  const modal = new bootstrap.Modal(document.getElementById('categoryModal'));
  const form = document.getElementById('categoryForm');
  let categories = [];
  let skills = [];

  async function load() {
    [categories, skills] = await Promise.all([
      api.get('/api/admin/categories'),
      api.get('/api/admin/skills'),
    ]);
    render();
  }

  function render() {
    const grid = document.getElementById('categoriesGrid');
    grid.innerHTML = categories.map((c) => {
      const count = skills.filter((s) => s.categoryId === c.id).length;
      return `
      <div class="col-md-6 col-xl-4">
        <div class="sw-card h-100">
          <div class="sw-card-body">
            <div class="d-flex justify-content-between align-items-start">
              <div class="sw-stat-icon sw-icon-indigo" style="width:38px;height:38px;font-size:15px;"><i class="fa-solid fa-tags"></i></div>
              <button class="btn btn-light btn-sm text-danger" onclick="window.__deleteCategory(${c.id}, '${escapeHtml(c.name).replace(/'/g, "\\'")}')"><i class="fa-solid fa-trash"></i></button>
            </div>
            <h6 class="mt-3 mb-1">${escapeHtml(c.name)}</h6>
            <div class="sw-text-muted" style="font-size: 13px;">${count} skill${count === 1 ? '' : 's'}</div>
          </div>
        </div>
      </div>`;
    }).join('');
  }

  window.__deleteCategory = async (id, name) => {
    if (!confirm(`Delete category "${name}"? Skills in this category will be affected.`)) return;
    try {
      await api.del('/api/admin/categories/' + id);
      toast.success('Category deleted');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  document.getElementById('addCategoryBtn').addEventListener('click', () => {
    form.reset();
    modal.show();
  });

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
      await api.post('/api/admin/categories', { name: document.getElementById('categoryName').value.trim() });
      toast.success('Category created');
      modal.hide();
      load();
    } catch (err) {
      toast.error(err.message);
    }
  });

  await load();
})();
