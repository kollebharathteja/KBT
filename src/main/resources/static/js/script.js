const API_BASE = window.location.origin + '/api/auth';

const loginForm = document.getElementById('loginForm');
const signupForm = document.getElementById('signupForm');
const showSignup = document.getElementById('showSignup');
const showLogin = document.getElementById('showLogin');
const loginError = document.getElementById('loginError');
const signupError = document.getElementById('signupError');

showSignup.addEventListener('click', (e) => {
  e.preventDefault();
  loginForm.classList.remove('active');
  signupForm.classList.add('active');
});

showLogin.addEventListener('click', (e) => {
  e.preventDefault();
  signupForm.classList.remove('active');
  loginForm.classList.add('active');
});

document.getElementById('forgotLink').addEventListener('click', (e) => {
  e.preventDefault();
  alert('Password reset flow goes here (e.g. email a reset link).');
});

function setButtonLoading(btn, loading, label) {
  btn.disabled = loading;
  btn.textContent = loading ? '...' : label;
}

loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  loginError.textContent = '';

  const emailId = document.getElementById('loginEmail').value.trim();
  const password = document.getElementById('loginPassword').value;
  const rememberMe = document.getElementById('rememberMe').checked;
  const btn = document.getElementById('loginBtn');

  setButtonLoading(btn, true, 'LOGIN');
  try {
    const res = await fetch(`${API_BASE}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ emailId, password, rememberMe })
    });
    const data = await res.json();

    if (!res.ok) {
      loginError.textContent = data.message || 'Login failed.';
      return;
    }

    const storage = rememberMe ? localStorage : sessionStorage;
    storage.setItem('kbt_token', data.token);
    storage.setItem('kbt_username', data.username);
    storage.setItem('kbt_role', data.role);

    if (data.role === 'SUPER_ADMIN') {
      window.location.href = 'admin.html';
    } else {
      window.location.href = 'dashboard.html';
    }
  } catch (err) {
    loginError.textContent = 'Could not reach the server. Please try again.';
  } finally {
    setButtonLoading(btn, false, 'LOGIN');
  }
});

signupForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  signupError.textContent = '';

  const username = document.getElementById('signupUsername').value.trim();
  const email = document.getElementById('signupEmail').value.trim();
  const password = document.getElementById('signupPassword').value;
  const btn = document.getElementById('signupBtn');

  setButtonLoading(btn, true, 'SIGN UP');
  try {
    const res = await fetch(`${API_BASE}/signup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password })
    });
    const data = await res.json();

    if (!res.ok) {
      signupError.textContent = data.message || 'Signup failed.';
      return;
    }

    sessionStorage.setItem('kbt_token', data.token);
    sessionStorage.setItem('kbt_username', data.username);
    sessionStorage.setItem('kbt_role', data.role);
    window.location.href = 'dashboard.html';
  } catch (err) {
    signupError.textContent = 'Could not reach the server. Please try again.';
  } finally {
    setButtonLoading(btn, false, 'SIGN UP');
  }
});
