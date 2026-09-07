(async function () {
  const user = await initShell({ title: 'Profile', active: 'profile' });
  if (!user) return;

  const content = document.getElementById('swContent');
  content.appendChild(document.getElementById('tpl-profile').content.cloneNode(true));

  async function load() {
    const profile = await api.get('/api/profile');
    document.getElementById('fullName').value = profile.fullName || '';
    document.getElementById('email').value = profile.email || '';
    document.getElementById('phone').value = profile.phone || '';
    document.getElementById('location').value = profile.location || '';
    document.getElementById('headline').value = profile.headline || '';
    document.getElementById('bio').value = profile.bio || '';
    document.getElementById('yearsOfExperience').value = profile.yearsOfExperience ?? '';
    document.getElementById('currentJobTitle').value = profile.currentJobTitle || '';

    document.getElementById('profileAvatar').textContent = initials(profile.fullName);
    document.getElementById('profileNameDisplay').textContent = profile.fullName;
    document.getElementById('profileHeadlineDisplay').textContent = profile.headline || 'No headline set yet';
    document.getElementById('completionBar').style.width = profile.profileCompletionPercent + '%';
    document.getElementById('completionPercent').textContent = profile.profileCompletionPercent + '%';
  }

  document.getElementById('profileForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = document.getElementById('saveProfileBtn');
    btn.disabled = true;
    const original = btn.textContent;
    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-1"></span> Saving...';

    const payload = {
      fullName: document.getElementById('fullName').value.trim(),
      phone: document.getElementById('phone').value.trim() || null,
      location: document.getElementById('location').value.trim() || null,
      headline: document.getElementById('headline').value.trim() || null,
      bio: document.getElementById('bio').value.trim() || null,
      yearsOfExperience: document.getElementById('yearsOfExperience').value ? parseInt(document.getElementById('yearsOfExperience').value, 10) : null,
      currentJobTitle: document.getElementById('currentJobTitle').value.trim() || null,
    };

    try {
      await api.put('/api/profile', payload);
      toast.success('Profile updated');
      await load();
    } catch (err) {
      toast.error(err.message || 'Could not update profile');
    } finally {
      btn.disabled = false;
      btn.textContent = original;
    }
  });

  await load();
})();
