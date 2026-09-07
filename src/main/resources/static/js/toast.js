/**
 * Lightweight toast notifications. Usage: toast.success('Saved'), toast.error('Oops')
 */
const toast = (() => {
  function ensureStack() {
    let stack = document.querySelector('.sw-toast-stack');
    if (!stack) {
      stack = document.createElement('div');
      stack.className = 'sw-toast-stack';
      document.body.appendChild(stack);
    }
    return stack;
  }

  function show(message, type = 'default', timeout = 3800) {
    const stack = ensureStack();
    const el = document.createElement('div');
    el.className = `sw-toast sw-toast-${type}`;
    const icon = type === 'success' ? 'fa-circle-check' : type === 'error' ? 'fa-circle-exclamation' : type === 'warning' ? 'fa-triangle-exclamation' : 'fa-circle-info';
    el.innerHTML = `<i class="fa-solid ${icon} mt-1"></i><div>${escapeHtml(message)}</div>`;
    stack.appendChild(el);
    setTimeout(() => {
      el.style.opacity = '0';
      el.style.transform = 'translateY(-6px)';
      el.style.transition = 'all 0.2s ease';
      setTimeout(() => el.remove(), 200);
    }, timeout);
  }

  function escapeHtml(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
  }

  return {
    success: (msg) => show(msg, 'success'),
    error: (msg) => show(msg, 'error'),
    warning: (msg) => show(msg, 'warning'),
    info: (msg) => show(msg, 'default'),
  };
})();
