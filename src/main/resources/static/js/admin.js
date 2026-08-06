const API_BASE = window.location.origin + '/api/admin';

const token = localStorage.getItem('kbt_token') || sessionStorage.getItem('kbt_token');
const role = localStorage.getItem('kbt_role') || sessionStorage.getItem('kbt_role');

if (!token || role !== 'SUPER_ADMIN') {
  window.location.href = 'index.html';
}

function authHeaders() {
  return { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token };
}

async function loadContent() {
  const res = await fetch(`${API_BASE}/content`, { headers: authHeaders() });
  if (!res.ok) return;
  const items = await res.json();
  const tbody = document.querySelector('#contentTable tbody');
  tbody.innerHTML = '';
  items.forEach(item => {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td>${item.key}</td><td>${item.section || ''}</td><td>${item.value}</td>
      <td><button class="del" data-id="${item.id}">Delete</button></td>`;
    tbody.appendChild(tr);
  });
  tbody.querySelectorAll('.del').forEach(btn => {
    btn.addEventListener('click', async () => {
      await fetch(`${API_BASE}/content/${btn.dataset.id}`, { method: 'DELETE', headers: authHeaders() });
      loadContent();
    });
  });
}

async function loadUsers() {
  const res = await fetch(`${API_BASE}/users`, { headers: authHeaders() });
  if (!res.ok) return;
  const users = await res.json();
  const tbody = document.querySelector('#usersTable tbody');
  tbody.innerHTML = '';
  users.forEach(u => {
    const tr = document.createElement('tr');
    tr.innerHTML = `<td>${u.username}</td><td>${u.email || ''}</td><td>${u.role}</td>
      <td>${u.role === 'SUPER_ADMIN' ? '' : `<button class="del" data-id="${u.id}">Delete</button>`}</td>`;
    tbody.appendChild(tr);
  });
  tbody.querySelectorAll('.del').forEach(btn => {
    btn.addEventListener('click', async () => {
      await fetch(`${API_BASE}/users/${btn.dataset.id}`, { method: 'DELETE', headers: authHeaders() });
      loadUsers();
    });
  });
}

document.getElementById('saveContentBtn').addEventListener('click', async () => {
  const key = document.getElementById('ckey').value.trim();
  const section = document.getElementById('csection').value.trim();
  const value = document.getElementById('cvalue').value.trim();
  if (!key || !value) return alert('Key and value are required.');

  await fetch(`${API_BASE}/content`, {
    method: 'POST',
    headers: authHeaders(),
    body: JSON.stringify({ key, section, value })
  });

  document.getElementById('ckey').value = '';
  document.getElementById('csection').value = '';
  document.getElementById('cvalue').value = '';
  loadContent();
});

document.getElementById('logoutBtn').addEventListener('click', () => {
  localStorage.clear();
  sessionStorage.clear();
  window.location.href = 'index.html';
});

loadContent();
loadUsers();
