const token = localStorage.getItem('kbt_token') || sessionStorage.getItem('kbt_token');
const username = localStorage.getItem('kbt_username') || sessionStorage.getItem('kbt_username');
const role = localStorage.getItem('kbt_role') || sessionStorage.getItem('kbt_role');

if (!token) {
  window.location.href = 'index.html';
}

document.getElementById('welcomeMsg').textContent = 'Welcome, ' + username;
document.getElementById('profUsername').textContent = username || '-';
document.getElementById('profRole').textContent = role || '-';

// ---- Tab switching ----
document.querySelectorAll('.nav-tab').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.nav-tab').forEach(b => b.classList.remove('active'));
    document.querySelectorAll('.page-tab').forEach(t => t.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById('tab-' + btn.dataset.tab).classList.add('active');
  });
});

// ---- Logout ----
document.getElementById('logoutBtn').addEventListener('click', () => {
  localStorage.clear();
  sessionStorage.clear();
  window.location.href = 'index.html';
});

// ---- Load the site grid (managed by K) ----
async function loadSites() {
  const grid = document.getElementById('siteGrid');
  try {
    const res = await fetch(window.location.origin + '/api/websites');
    const sites = await res.json();

    if (!sites.length) {
      grid.innerHTML = '<p class="empty-state">No sites added yet. Ask K to add some from the control panel.</p>';
      return;
    }

    grid.innerHTML = sites.map(site => `
      <a class="site-card" href="${site.url}" target="_blank" rel="noopener">
        ${site.imageUrl ? `<img class="site-card-img" src="${site.imageUrl}" alt="">` : ''}
        <p class="site-title">${site.title}</p>
        <p class="site-desc">${site.description || ''}</p>
      </a>
    `).join('');
  } catch (err) {
    grid.innerHTML = '<p class="empty-state">Could not load sites right now.</p>';
  }
}

loadSites();
