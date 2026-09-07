(async function () {
  const user = await initShell({ title: 'Dashboard', active: 'dashboard' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-dashboard').content.cloneNode(true));

  document.getElementById('welcomeName').textContent = user.fullName.split(' ')[0];

  try {
    const dash = await api.get('/api/dashboard');
    renderStats(dash);
    renderCareerGoal(dash);
    renderSkillGap(dash);
    renderActivity(dash.recentActivity);
    renderProgressChart(dash.progressChart);
  } catch (err) {
    toast.error(err.message || 'Could not load dashboard data');
  }

  function renderStats(dash) {
    document.getElementById('statProfile').textContent = dash.profileCompletionPercent + '%';
    document.getElementById('statReadiness').textContent = dash.careerReadinessPercent != null ? dash.careerReadinessPercent + '%' : '—';
    document.getElementById('statSkills').textContent = dash.totalSkills;
    document.getElementById('statCerts').textContent = dash.totalCertifications;
  }

  function renderCareerGoal(dash) {
    const body = document.getElementById('careerGoalBody');
    if (!dash.targetRoleName) {
      body.innerHTML = `
        <div class="sw-empty-state py-3">
          <i class="fa-solid fa-flag"></i>
          <h6>No career goal set yet</h6>
          <p class="mb-3" style="font-size: 13.5px;">Choose a target role to unlock your readiness score.</p>
          <a href="/pages/career-goals.html" class="btn btn-primary btn-sm">Set a Career Goal</a>
        </div>`;
      return;
    }
    const readiness = dash.careerReadinessPercent || 0;
    body.innerHTML = `
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <div class="sw-eyebrow">Target Role</div>
          <div class="fw-bold" style="color: var(--sw-navy-900); font-size: 18px;">${escapeHtml(dash.targetRoleName)}</div>
        </div>
        <div class="text-end">
          <div class="fw-bold" style="font-size: 22px; color: var(--sw-indigo-600);">${readiness}%</div>
          <div class="sw-text-muted" style="font-size:12px;">readiness</div>
        </div>
      </div>
      <div class="sw-progress mb-3"><div class="sw-progress-bar" style="width: ${readiness}%;"></div></div>
      <div class="d-flex gap-2 flex-wrap">
        <span class="sw-badge sw-badge-success"><span class="sw-badge-dot"></span>${dash.strongSkills.length} Strong</span>
        <span class="sw-badge sw-badge-warning"><span class="sw-badge-dot"></span>${dash.skillsToImprove.length} To improve</span>
        <span class="sw-badge sw-badge-error"><span class="sw-badge-dot"></span>${dash.missingSkills.length} Missing</span>
      </div>`;
  }

  function renderSkillGap(dash) {
    const el = document.getElementById('skillGapSnapshot');
    if (!dash.targetRoleName) {
      el.innerHTML = `<p class="sw-text-muted mb-0" style="font-size: 13.5px;">Set a career goal to see your skill-gap breakdown here.</p>`;
      return;
    }
    function section(title, items, badgeClass) {
      if (!items.length) return '';
      return `<div class="mb-3">
        <div class="fw-semibold mb-2" style="font-size:12.5px; color: var(--sw-slate-500); text-transform:uppercase; letter-spacing:0.04em;">${title}</div>
        <div class="d-flex flex-wrap gap-1">
          ${items.map((s) => `<span class="sw-badge ${badgeClass}"><span class="sw-badge-dot"></span>${escapeHtml(s)}</span>`).join('')}
        </div>
      </div>`;
    }
    const html = section('Strong', dash.strongSkills, 'sw-badge-success')
      + section('Needs Improvement', dash.skillsToImprove, 'sw-badge-warning')
      + section('Missing', dash.missingSkills, 'sw-badge-error');
    el.innerHTML = html || '<p class="sw-text-muted mb-0" style="font-size:13.5px;">No required skills configured for this role yet.</p>';
  }

  function renderActivity(activity) {
    const el = document.getElementById('activityList');
    if (!activity || !activity.length) {
      el.innerHTML = `<div class="sw-empty-state"><i class="fa-solid fa-clock-rotate-left"></i><h6>No activity yet</h6><p class="mb-0" style="font-size:13.5px;">Actions you take will show up here.</p></div>`;
      return;
    }
    el.innerHTML = activity.map((a) => `
      <div class="d-flex align-items-start gap-3 px-4 py-3" style="border-bottom: 1px solid var(--sw-slate-100);">
        <div class="sw-stat-icon sw-icon-indigo" style="width:32px;height:32px;font-size:13px;flex-shrink:0;"><i class="fa-solid fa-bolt"></i></div>
        <div>
          <div style="font-size: 13.5px; color: var(--sw-slate-800);">${escapeHtml(a.description)}</div>
          <div class="sw-text-muted" style="font-size: 12px;">${formatDateTime(a.createdAt)}</div>
        </div>
      </div>`).join('');
  }

  function renderProgressChart(progressChart) {
    const withHistory = (progressChart || []).filter((p) => p.history && p.history.length > 0);
    if (!withHistory.length) {
      document.getElementById('progressChart').classList.add('d-none');
      document.getElementById('progressChartEmpty').classList.remove('d-none');
      return;
    }

    // Build a unified set of dates across all skills, and a dataset per skill (up to 6 for readability)
    const skills = withHistory.slice(0, 6);
    const allDates = Array.from(new Set(skills.flatMap((s) => s.history.map((h) => h.recordedDate)))).sort();

    const palette = ['#4f46e5', '#14b8a6', '#d97706', '#dc2626', '#0ea5e9', '#a855f7'];
    const datasets = skills.map((skill, idx) => {
      const byDate = {};
      skill.history.forEach((h) => { byDate[h.recordedDate] = h.proficiencyWeight; });
      let lastKnown = null;
      const data = allDates.map((d) => {
        if (byDate[d] !== undefined) lastKnown = byDate[d];
        return lastKnown;
      });
      return {
        label: skill.skillName,
        data,
        borderColor: palette[idx % palette.length],
        backgroundColor: palette[idx % palette.length],
        tension: 0.35,
        spanGaps: true,
        pointRadius: 3,
      };
    });

    new Chart(document.getElementById('progressChart'), {
      type: 'line',
      data: { labels: allDates.map(formatDate), datasets },
      options: {
        responsive: true,
        plugins: { legend: { position: 'bottom', labels: { boxWidth: 10, font: { size: 11 } } } },
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
  }
})();
