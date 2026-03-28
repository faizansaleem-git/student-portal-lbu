# Student Portal — LBU Microservices Project
**MSc Software Engineering for Service Computing**
---
## Project Overview
A microservices-based student portal system built as a group assignment. The system consists of three independently running microservices that communicate via REST APIs.
---
## Team & Microservice Ownership
| Member | Microservice | Technology | Port |
|---|---|---|---|
| Faizan Saleem | Student Portal + Frontend | Spring Boot + HTML/CSS/JS | 8080 |
| Faizan Zafar | Finance Service | Spring Boot + Docker + MariaDB | 8081 |
| Hiral Jahlani | Library Service | Python Flask + MySQL | 8082 |
---
## Microservices Architecture

[ Frontend - HTML/CSS/JS ]
         |
         ▼
[ Student Portal - Port 8080 ]  ──────→  [ Finance Service - Port 8081 ]
         |                                        ▲
         └──────────────────────────────→  [ Library Service - Port 8082 ]
                                                  |
                                                  └──→ [ Finance Service ]
                                                        (late book fines)

---
## Student Portal — Faizan Saleem
**Technology:** Java 21, Spring Boot 3.5.11, MySQL 8.0.45, Spring Security, JWT
### Features
- [x] User Registration
- [x] User Login with JWT Authentication
- [x] View Available Courses
- [x] Enrol in a Course
- [x] View My Enrolments
- [x] View / Update Student Profile
- [x] Graduation Eligibility Check
### API Endpoints
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | /api/auth/register | Register new student | No |
| POST | /api/auth/login | Login and receive JWT | No |
| GET | /api/courses | View all courses | No |
| POST | /api/enrolments | Enrol in a course | Yes |
| GET | /api/enrolments | View my enrolments | Yes |
| GET | /api/profile | View student profile | Yes |
| PUT | /api/profile | Update student profile | Yes |
| GET | /api/graduation | Check graduation eligibility | Yes |
### Integration Points
- On **register** → creates account in Finance service + Library service
- On **enrolment** → creates invoice in Finance service
- On **graduation check** → checks outstanding balance in Finance service
### Testing
8 JUnit tests passing:
- AuthServiceTest — 6 tests (register, login, validation)
- CourseServiceTest — 2 tests (course retrieval)
---
## Finance Service — Faizan Zafar
**Technology:** Spring Boot, Docker, MariaDB | **Port:** 8081
### Responsibilities
- Create student finance account (called on student registration)
- Create invoice (called on course enrolment and late library fines)
- Check outstanding balance (called on graduation eligibility check)
---
## Library Service — Hiral Jahlani
**Technology:** Python Flask, MySQL | **Port:** 8082
### Responsibilities
- Create student library account (called on student registration)
- Manage book borrowing and returns
- Issue late return fines → calls Finance service to create invoice
---
## Prerequisites
| Software | Version |
|---|---|
| Java | 21 (Temurin) |
| MySQL | 8.0.45 |
| Docker Desktop | 29.2.1 |
| Python | 3.x |
| Maven | Latest |
---
## How to Run
### 1. Finance Service (Docker)
bash
cd finance-master
docker-compose up

### 2. Library Service (Python)
bash
cd CESBooks-master
pip install -r requirements.txt
$env:DATABASE="mysql://root:YOUR_PASSWORD@localhost:3306/library"
python waitress-server.py

### 3. Student Portal (IntelliJ / Maven)
Open the student-portal folder in IntelliJ IDEA and click the Run button.
App starts on http://localhost:8080
### 4. Frontend
Open student-portal-frontend/index.html with VS Code Live Server.
Runs on http://127.0.0.1:5500
---
