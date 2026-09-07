/**
 * Renders the sidebar + topbar shell for authenticated pages and guards
 * access (redirects to login if there's no active session, and to the
 * dashboard if a non-admin somehow lands on an admin page).
 *
 * Usage: call `initShell({ title: 'Dashboard', active: 'dashboard' })`
 * near the top of a page's script. It returns a Promise<currentUser>.
 */

const USER_NAV = [
  { key: 'dashboard', href: '/pages/dashboard.html', icon: 'fa-gauge-high', label: 'Dashboard' },
  { key: 'skills', href: '/pages/skills.html', icon: 'fa-layer-group', label: 'My Skills' },
  { key: 'career-goals', href: '/pages/career-goals.html', icon: 'fa-flag', label: 'Career Goals' },
  { key: 'skill-gap', href: '/pages/skill-gap.html', icon: 'fa-chart-simple', label: 'Skill Gap Analysis' },
  { key: 'progress', href: '/pages/progress.html', icon: 'fa-chart-line', label: 'Progress' },
  { key: 'certifications', href: '/pages/certifications.html', icon: 'fa-certificate', label: 'Certifications' },
  { key: 'achievements', href: '/pages/achievements.html', icon: 'fa-trophy', label: 'Achievements' },
];

const USER_NAV_BOTTOM = [
  { key: 'profile', href: '/pages/profile.html', icon: 'fa-user', label: 'Profile' },
  { key: 'settings', href: '/pages/settings.html', icon: 'fa-gear', label: 'Settings' },
];

const ADMIN_NAV = [
  { key: 'admin-dashboard', href: '/pages/admin-dashboard.html', icon: 'fa-gauge-high', label: 'Admin Dashboard' },
  { key: 'admin-users', href: '/pages/admin-users.html', icon: 'fa-users', label: 'Users' },
  { key: 'admin-skills', href: '/pages/admin-skills.html', icon: 'fa-layer-group', label: 'Skills' },
  { key: 'admin-categories', href: '/pages/admin-categories.html', icon: 'fa-tags', label: 'Categories' },
  { key: 'admin-career-roles', href: '/pages/admin-career-roles.html', icon: 'fa-briefcase', label: 'Career Roles' },
];

function initials(name) {
  if (!name) return '?';
  const parts = name.trim().split(/\s+/);
  return ((parts[0]?.[0] || '') + (parts[1]?.[0] || '')).toUpperCase();
}

function navLinkHtml(item, active) {
  return `<a href="${item.href}" class="sw-nav-link ${active === item.key ? 'active' : ''}">
    <i class="fa-solid ${item.icon}"></i><span>${item.label}</span>
  </a>`;
}

async function initShell({ title, active, requireAdmin = false }) {
  let user;
  try {
    user = await api.get('/api/auth/me');
  } catch (e) {
    window.location.href = '/pages/login.html';
    return null;
  }

  const isAdmin = user.role === 'ROLE_ADMIN';
  if (requireAdmin && !isAdmin) {
    window.location.href = '/pages/dashboard.html';
    return null;
  }

  const navItems = isAdmin ? ADMIN_NAV : USER_NAV;

  const shellHtml = `
    <div class="sw-sidebar" id="swSidebar">
      <div class="sw-sidebar-brand">
        <div class="sw-logo-mark">CL</div>
        <div class="sw-brand-text">Clario</div>
      </div>
      <div class="sw-nav-section-label">${isAdmin ? 'Administration' : 'Workspace'}</div>
      <nav class="sw-nav">
        ${navItems.map((item) => navLinkHtml(item, active)).join('')}
        ${!isAdmin ? `<div class="sw-nav-section-label">Account</div>${USER_NAV_BOTTOM.map((item) => navLinkHtml(item, active)).join('')}` : ''}
      </nav>
      <div class="sw-sidebar-footer">
        <div class="d-flex align-items-center gap-2 mb-3">
          <div class="sw-avatar">${initials(user.fullName)}</div>
          <div class="flex-grow-1 overflow-hidden">
            <div class="text-white fw-semibold sw-truncate" style="font-size:13.5px;">${escapeHtml(user.fullName)}</div>
            <div class="sw-truncate" style="font-size:11.5px; color:#7c88a3;">${escapeHtml(user.email)}</div>
          </div>
        </div>
        <button class="btn btn-light btn-sm w-100" id="swLogoutBtn"><i class="fa-solid fa-arrow-right-from-bracket me-1"></i> Log out</button>
      </div>
    </div>
    <div class="sw-main">
      <div class="sw-topbar">
        <div class="d-flex align-items-center gap-3">
          <button class="btn btn-light btn-icon sw-sidebar-toggle" id="swSidebarToggle"><i class="fa-solid fa-bars"></i></button>
          <div class="sw-breadcrumb-title">${escapeHtml(title)}</div>
        </div>
        <div class="d-flex align-items-center gap-2">
          ${!isAdmin && !requireAdmin ? '' : ''}
          <span class="sw-badge ${isAdmin ? 'sw-badge-indigo' : 'sw-badge-neutral'}"><span class="sw-badge-dot"></span>${isAdmin ? 'Administrator' : 'Member'}</span>
        </div>
      </div>
      <div class="sw-content" id="swContent"></div>
    </div>
  `;

  document.getElementById('swShellRoot').innerHTML = shellHtml;

  document.getElementById('swLogoutBtn').addEventListener('click', async () => {
    try {
      await api.post('/api/auth/logout');
    } catch (e) {
      // ignore - we redirect regardless
    }
    window.location.href = '/index.html';
  });

  const toggleBtn = document.getElementById('swSidebarToggle');
  if (toggleBtn) {
    toggleBtn.addEventListener('click', () => {
      document.getElementById('swSidebar').classList.toggle('sw-sidebar-open');
    });
  }

  return user;
}

function escapeHtml(str) {
  const div = document.createElement('div');
  div.textContent = str == null ? '' : String(str);
  return div.innerHTML;
}

function levelPillClass(level) {
  switch ((level || '').toUpperCase()) {
    case 'BEGINNER': return 'sw-level-beginner';
    case 'INTERMEDIATE': return 'sw-level-intermediate';
    case 'ADVANCED': return 'sw-level-advanced';
    case 'EXPERT': return 'sw-level-expert';
    default: return 'sw-level-beginner';
  }
}

function titleCase(str) {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

function formatDate(dateStr) {
  if (!dateStr) return '—';
  const d = new Date(dateStr);
  if (isNaN(d.getTime())) return dateStr;
  return d.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
}

function formatDateTime(dateStr) {
  if (!dateStr) return '—';
  const d = new Date(dateStr);
  if (isNaN(d.getTime())) return dateStr;
  return d.toLocaleString(undefined, { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
}
