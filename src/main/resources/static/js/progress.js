(async function () {
  const user = await initShell({ title: 'Progress', active: 'progress' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-progress').content.cloneNode(true));

  const listEl = document.getElementById('progressList');
  listEl.innerHTML = `<div class="sw-card"><div class="sw-card-body"><div class="sw-skeleton" style="height:120px;"></div></div></div>`;

  let progress = [];
  try {
    progress = await api.get('/api/progress');
  } catch (err) {
    toast.error(err.message || 'Could not load progress data');
    return;
  }

  if (!progress.length) {
    listEl.innerHTML = `<div class="sw-card"><div class="sw-empty-state py-5">
      <i class="fa-solid fa-chart-line"></i>
      <h6>No skills tracked yet</h6>
      <p class="mb-3" style="font-size:13.5px;">Add skills on the My Skills page to start building a progress history.</p>
      <a href="/pages/skills.html" class="btn btn-primary btn-sm">Go to My Skills</a>
    </div></div>`;
    return;
  }

  const palette = { BEGINNER: '#94a3b8', INTERMEDIATE: '#4f46e5', ADVANCED: '#0d9488', EXPERT: '#be185d' };

  listEl.innerHTML = `<div class="row g-3">${progress.map((p, idx) => `
    <div class="col-lg-6">
      <div class="sw-card h-100">
        <div class="sw-card-header">
          <h6>${escapeHtml(p.skillName)}</h6>
          ${p.history.length ? `<span class="sw-level-pill ${levelPillClass(p.history[p.history.length - 1].proficiencyLevel)}">${titleCase(p.history[p.history.length - 1].proficiencyLevel)}</span>` : ''}
        </div>
        <div class="sw-card-body">
          ${p.history.length ? `<canvas id="chart-${idx}" height="140"></canvas>` : `<p class="sw-text-muted mb-0" style="font-size:13.5px;">No history recorded for this skill yet — update its proficiency to start tracking.</p>`}
        </div>
      </div>
    </div>`).join('')}</div>`;

  progress.forEach((p, idx) => {
    if (!p.history.length) return;
    new Chart(document.getElementById('chart-' + idx), {
      type: 'line',
      data: {
        labels: p.history.map((h) => formatDate(h.recordedDate)),
        datasets: [{
          label: p.skillName,
          data: p.history.map((h) => h.proficiencyWeight),
          borderColor: '#4f46e5',
          backgroundColor: '#4f46e5',
          tension: 0.35,
          pointRadius: 4,
          pointBackgroundColor: p.history.map((h) => palette[h.proficiencyLevel] || '#4f46e5'),
        }],
      },
      options: {
        plugins: { legend: { display: false } },
        scales: {
          y: {
            min: 0, max: 4, ticks: {
              stepSize: 1,
              callback: (v) => ({ 0: '', 1: 'Beginner', 2: 'Intermediate', 3: 'Advanced', 4: 'Expert' }[v] || ''),
            },
          },
        },
      },
    });
  });
})();
