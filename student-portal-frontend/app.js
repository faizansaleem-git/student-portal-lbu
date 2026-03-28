const API = 'http://localhost:8080';

// ─── AUTH ───────────────────────────────────────────

function showRegister() {
    document.getElementById('loginForm').classList.add('hidden');
    document.getElementById('registerForm').classList.remove('hidden');
    hideAlert();
}

function showLogin() {
    document.getElementById('registerForm').classList.add('hidden');
    document.getElementById('loginForm').classList.remove('hidden');
    hideAlert();
}

async function login() {
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    if (!username || !password) {
        showAlert('Please enter username and password', 'error');
        return;
    }

    try {
        const res = await fetch(`${API}/api/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        const data = await res.json();

        if (res.ok) {
            localStorage.setItem('token', data.token);
            localStorage.setItem('username', data.username);
            window.location.href = 'dashboard.html';
        } else {
            showAlert('Invalid username or password', 'error');
        }
    } catch (e) {
        showAlert('Cannot connect to server', 'error');
    }
}

async function register() {
    const username = document.getElementById('regUsername').value;
    const password = document.getElementById('regPassword').value;
    const firstName = document.getElementById('regFirstName').value;
    const lastName = document.getElementById('regLastName').value;
    const email = document.getElementById('regEmail').value;

    if (!username || !password || !firstName || !lastName || !email) {
        showAlert('Please fill in all fields', 'error');
        return;
    }

    try {
        const res = await fetch(`${API}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, firstName, lastName, email })
        });

        const data = await res.json();

        if (res.ok) {
            showAlert('Registration successful! Please login.', 'success');
            setTimeout(showLogin, 1500);
        } else {
            showAlert(data.message || 'Registration failed', 'error');
        }
    } catch (e) {
        showAlert('Cannot connect to server', 'error');
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    window.location.href = 'index.html';
}

// ─── DASHBOARD ───────────────────────────────────────

function getToken() {
    return localStorage.getItem('token');
}

function authHeaders() {
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${getToken()}`
    };
}

function showSection(name) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.getElementById(name).classList.add('active');
    event.target.classList.add('active');

    if (name === 'courses') loadCourses();
    if (name === 'enrolments') loadEnrolments();
    if (name === 'profile') loadProfile();
}

// ─── COURSES ─────────────────────────────────────────

async function loadCourses() {
    try {
        const res = await fetch(`${API}/api/courses`);
        const courses = await res.json();
        const tbody = document.getElementById('coursesTable');
        tbody.innerHTML = courses.map(c => `
            <tr>
                <td>${c.code}</td>
                <td>${c.name}</td>
                <td>${c.description}</td>
                <td>${c.credits}</td>
                <td><button class="btn btn-small btn-enrol" onclick="enrol(${c.id})">Enrol</button></td>
            </tr>
        `).join('');
    } catch (e) {
        document.getElementById('coursesTable').innerHTML = '<tr><td colspan="5">Failed to load courses</td></tr>';
    }
}

async function enrol(courseId) {
    try {
        const res = await fetch(`${API}/api/enrolments`, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify({ courseId })
        });

        if (res.ok) {
            showAlert('Successfully enrolled!', 'success');
            loadEnrolments();
        } else {
            const data = await res.json();
            showAlert(data.message || 'Enrolment failed', 'error');
        }
    } catch (e) {
        showAlert('Cannot connect to server', 'error');
    }
}

// ─── ENROLMENTS ──────────────────────────────────────

async function loadEnrolments() {
    try {
        const res = await fetch(`${API}/api/enrolments`, { headers: authHeaders() });
        const enrolments = await res.json();
        const tbody = document.getElementById('enrolmentsTable');

        if (enrolments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align:center; color:#888;">No enrolments yet</td></tr>';
            return;
        }

        tbody.innerHTML = enrolments.map(e => `
            <tr>
                <td>${e.courseCode}</td>
                <td>${e.courseName}</td>
                <td>${e.credits}</td>
                <td>${new Date(e.enrolmentDate).toLocaleDateString()}</td>
            </tr>
        `).join('');
    } catch (e) {
        document.getElementById('enrolmentsTable').innerHTML = '<tr><td colspan="4">Failed to load enrolments</td></tr>';
    }
}

// ─── PROFILE ─────────────────────────────────────────

async function loadProfile() {
    try {
        const res = await fetch(`${API}/api/profile`, { headers: authHeaders() });
        const data = await res.json();

        document.getElementById('profileFirstName').value = data.firstName;
        document.getElementById('profileLastName').value = data.lastName;
        document.getElementById('profileEmail').value = data.email;
        document.getElementById('profileUsername').value = data.username;
    } catch (e) {
        showAlert('Failed to load profile', 'error');
    }
}

async function updateProfile() {
    const firstName = document.getElementById('profileFirstName').value;
    const lastName = document.getElementById('profileLastName').value;
    const email = document.getElementById('profileEmail').value;

    try {
        const res = await fetch(`${API}/api/profile`, {
            method: 'PUT',
            headers: authHeaders(),
            body: JSON.stringify({ firstName, lastName, email })
        });

        if (res.ok) {
            showAlert('Profile updated successfully!', 'success');
        } else {
            showAlert('Failed to update profile', 'error');
        }
    } catch (e) {
        showAlert('Cannot connect to server', 'error');
    }
}

// ─── GRADUATION ──────────────────────────────────────

async function checkGraduation() {
    try {
        const res = await fetch(`${API}/api/graduation`, { headers: authHeaders() });
        const data = await res.json();

        const div = document.getElementById('graduationResult');
        const icon = data.eligible ? '🎓' : '📚';
        const cls = data.eligible ? 'eligible' : 'not-eligible';

        div.innerHTML = `
            <div class="status">${icon}</div>
            <h3 class="${cls}">${data.message}</h3>
            <p style="margin-top:10px; color:#666;">Credits: <strong>${data.totalCredits} / ${data.requiredCredits}</strong></p>
            <p style="color:#666;">Courses enrolled: <strong>${data.coursesEnrolled}</strong></p>
            <p style="margin-top:8px; color:#888; font-size:0.9rem;">${data.financeStatus}</p>
            <button class="btn" style="width:auto; padding:10px 25px; margin-top:20px;" onclick="checkGraduation()">Refresh</button>
        `;
    } catch (e) {
        showAlert('Cannot connect to server', 'error');
    }
}

// ─── HELPERS ─────────────────────────────────────────

function showAlert(message, type) {
    const alert = document.getElementById('alert');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;
    alert.classList.remove('hidden');
    setTimeout(hideAlert, 4000);
}

function hideAlert() {
    const alert = document.getElementById('alert');
    if (alert) alert.classList.add('hidden');
}

// Load courses when dashboard opens
if (window.location.pathname.includes('dashboard')) {
    if (!getToken()) {
        window.location.href = 'index.html';
    }
    loadCourses();
}