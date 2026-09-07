(async function () {
  const user = await initShell({ title: 'Achievements', active: 'achievements' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-achievements').content.cloneNode(true));

  const modal = new bootstrap.Modal(document.getElementById('achievementModal'));
  const form = document.getElementById('achievementForm');
  let achievements = [];

  async function load() {
    achievements = await api.get('/api/achievements');
    render();
  }

  function render() {
    const list = document.getElementById('achievementsList');
    const empty = document.getElementById('achievementsEmpty');
    if (!achievements.length) {
      list.innerHTML = '';
      empty.classList.remove('d-none');
      return;
    }
    empty.classList.add('d-none');
    list.innerHTML = achievements.map((a) => `
      <div class="d-flex align-items-start gap-3 px-4 py-3" style="border-bottom: 1px solid var(--sw-slate-100);">
        <div class="sw-stat-icon sw-icon-success" style="width:40px;height:40px;font-size:16px;flex-shrink:0;"><i class="fa-solid fa-trophy"></i></div>
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between align-items-start flex-wrap gap-2">
            <h6 class="mb-1">${escapeHtml(a.title)}</h6>
            <div>
              <button class="btn btn-light btn-sm me-1" onclick="window.__editAchievement(${a.id})"><i class="fa-solid fa-pen"></i></button>
              <button class="btn btn-light btn-sm text-danger" onclick="window.__deleteAchievement(${a.id}, '${escapeHtml(a.title).replace(/'/g, "\\'")}')"><i class="fa-solid fa-trash"></i></button>
            </div>
          </div>
          ${a.description ? `<p class="sw-text-soft mb-2" style="font-size: 13.5px;">${escapeHtml(a.description)}</p>` : ''}
          <div class="sw-text-muted" style="font-size: 12.5px;">
            ${a.achievementDate ? formatDate(a.achievementDate) : ''}${a.organization ? ' · ' + escapeHtml(a.organization) : ''}
          </div>
        </div>
      </div>`).join('');
  }

  window.__editAchievement = (id) => {
    const a = achievements.find((x) => x.id === id);
    if (!a) return;
    document.getElementById('achievementModalTitle').textContent = 'Edit Achievement';
    document.getElementById('achievementId').value = a.id;
    document.getElementById('achievementTitle').value = a.title;
    document.getElementById('achievementDescription').value = a.description || '';
    document.getElementById('achievementDate').value = a.achievementDate || '';
    document.getElementById('achievementOrg').value = a.organization || '';
    modal.show();
  };

  window.__deleteAchievement = async (id, title) => {
    if (!confirm(`Delete "${title}"?`)) return;
    try {
      await api.del('/api/achievements/' + id);
      toast.success('Achievement removed');
      load();
    } catch (err) {
      toast.error(err.message);
    }
  };

  document.getElementById('addAchievementBtn').addEventListener('click', () => {
    form.reset();
    document.getElementById('achievementModalTitle').textContent = 'Add Achievement';
    document.getElementById('achievementId').value = '';
    modal.show();
  });

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('achievementId').value;
    const payload = {
      title: document.getElementById('achievementTitle').value.trim(),
      description: document.getElementById('achievementDescription').value.trim() || null,
      achievementDate: document.getElementById('achievementDate').value || null,
      organization: document.getElementById('achievementOrg').value.trim() || null,
    };
    try {
      if (id) {
        await api.put('/api/achievements/' + id, payload);
        toast.success('Achievement updated');
      } else {
        await api.post('/api/achievements', payload);
        toast.success('Achievement added');
      }
      modal.hide();
      load();
    } catch (err) {
      toast.error(err.message);
    }
  });

  await load();
})();
