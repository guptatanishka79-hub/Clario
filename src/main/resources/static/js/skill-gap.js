(async function () {
  const user = await initShell({ title: 'Skill Gap Analysis', active: 'skill-gap' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-skill-gap').content.cloneNode(true));

  const container = document.getElementById('gapContainer');
  container.innerHTML = `<div class="sw-card"><div class="sw-card-body"><div class="sw-skeleton" style="height: 140px;"></div></div></div>`;

  try {
    const report = await api.get('/api/skill-gap');
    render(report);
  } catch (err) {
    container.innerHTML = '';
    container.appendChild(document.getElementById('tpl-no-goal').content.cloneNode(true));
  }

  function statusBadge(status) {
    if (status === 'STRONG') return '<span class="sw-badge sw-badge-success"><span class="sw-badge-dot"></span>Strong</span>';
    if (status === 'NEEDS_IMPROVEMENT') return '<span class="sw-badge sw-badge-warning"><span class="sw-badge-dot"></span>Needs Improvement</span>';
    return '<span class="sw-badge sw-badge-error"><span class="sw-badge-dot"></span>Missing</span>';
  }

  function render(report) {
    const readiness = report.readinessPercent;
    container.innerHTML = `
      <div class="sw-card mb-3">
        <div class="sw-card-body">
          <div class="d-flex justify-content-between align-items-start flex-wrap gap-3 mb-3">
            <div>
              <div class="sw-eyebrow">Target Role</div>
              <div class="fw-bold" style="font-size: 22px; color: var(--sw-navy-900);">${escapeHtml(report.careerRoleName)}</div>
            </div>
            <div class="text-end">
              <div class="fw-bold" style="font-size: 32px; color: var(--sw-indigo-600); line-height:1;">${readiness}%</div>
              <div class="sw-text-muted" style="font-size: 12.5px;">career readiness</div>
            </div>
          </div>
          <div class="sw-progress mb-3"><div class="sw-progress-bar" style="width: ${readiness}%;"></div></div>
          <div class="d-flex gap-2 flex-wrap">
            <span class="sw-badge sw-badge-success"><span class="sw-badge-dot"></span>${report.strongSkills.length} Strong</span>
            <span class="sw-badge sw-badge-warning"><span class="sw-badge-dot"></span>${report.skillsToImprove.length} Needs Improvement</span>
            <span class="sw-badge sw-badge-error"><span class="sw-badge-dot"></span>${report.missingSkills.length} Missing</span>
          </div>
        </div>
      </div>

      <div class="sw-card mb-3">
        <div class="sw-card-header"><h6>Skill-by-Skill Breakdown</h6></div>
        <div class="table-responsive">
          <table class="sw-table">
            <thead>
              <tr><th>Skill</th><th>Required</th><th>Your Level</th><th>Contribution</th><th>Status</th></tr>
            </thead>
            <tbody>
              ${report.items.map((i) => `
                <tr>
                  <td class="fw-semibold" style="color: var(--sw-navy-900);">${escapeHtml(i.skillName)}</td>
                  <td><span class="sw-level-pill ${levelPillClass(i.requiredProficiency)}">${titleCase(i.requiredProficiency)}</span></td>
                  <td>${i.currentProficiency === 'NONE' ? '<span class="sw-text-muted">Not started</span>' : `<span class="sw-level-pill ${levelPillClass(i.currentProficiency)}">${titleCase(i.currentProficiency)}</span>`}</td>
                  <td>
                    <div class="d-flex align-items-center gap-2">
                      <div class="sw-progress sw-progress-thin" style="width: 80px;"><div class="sw-progress-bar" style="width:${Math.round(i.contributionRatio * 100)}%;"></div></div>
                      <span class="sw-text-muted" style="font-size:12.5px;">${Math.round(i.contributionRatio * 100)}%</span>
                    </div>
                  </td>
                  <td>${statusBadge(i.status)}</td>
                </tr>`).join('')}
            </tbody>
          </table>
        </div>
      </div>

      <div class="sw-card">
        <div class="sw-card-header"><h6>How this is calculated</h6></div>
        <div class="sw-card-body">
          <p class="sw-text-muted mb-2" style="font-size: 13.5px;">
            Each proficiency level has a weight: Beginner = 1, Intermediate = 2, Advanced = 3, Expert = 4.
            For every required skill, we compare your current weight to the required weight
            (<code>current ÷ required</code>, capped at 100%). Your overall readiness is the average of every
            required skill's contribution. No AI or machine learning is involved — it's simple, transparent math.
          </p>
        </div>
      </div>
    `;
  }
})();
