# Clario — Professional Skill & Career Management Platform

Clario helps individuals track their professional skills, set a target career role,
and see a transparent, explainable readiness score for that role — along with
certifications, achievements, and skill-progress history over time.

This is **not** a social platform: there is no chat, no feed, no connections, and no
peer-to-peer messaging. It is a focused, single-user-at-a-time career management tool
with a lightweight admin console for managing the shared catalog (skills, categories,
and career roles).

---

## 1. Project Overview

- **Backend:** Java 17, Spring Boot 3.3, Spring Web, Spring Data JPA (Hibernate), Spring
  Security, Maven, session-based authentication with BCrypt password hashing.
- **Database:** MySQL, normalized relational schema with foreign keys, managed by
  Hibernate (`ddl-auto=update`) plus an idempotent `data.sql` seed script.
- **Frontend:** Plain HTML5, CSS3, vanilla JavaScript, and Bootstrap 5 (extensively
  re-skinned with a custom design system — see `css/style.css`). No React/Angular/Vue.
- **Charts:** Chart.js, fed entirely from live backend data (no hardcoded values).

---

## 2. Features

### Authentication & Authorization
- Registration with full name, email, password, and confirm-password validation.
- Session-based login/logout (Spring Security), BCrypt password hashing.
- Two roles: `ROLE_USER` and `ROLE_ADMIN`, enforced server-side — `/api/admin/**` is
  only reachable by admins, and the frontend hides admin navigation from regular users.

### For every user
- **Profile** — contact details, headline, bio, experience, current job title, with a
  dynamically calculated profile-completion percentage.
- **Skill Management** — add/edit/delete skills with proficiency level, years of
  experience, category, search, and category filtering.
- **Career Goals** — browse predefined career roles and their required skills, and set
  one as your target role.
- **Skill Gap Analysis** — a transparent, fully explainable comparison between your
  current skills and your target role's requirements (see §7 below).
- **Progress Tracking** — every proficiency change is recorded in a history table, shown
  as a trend line per skill.
- **Certifications** — name, issuer, issue/expiry dates, credential ID and URL, with
  automatic "expired" detection.
- **Achievements** — title, description, date, and organization/project.
- **Dashboard** — summary cards, career goal card with a progress bar, a skill-gap
  snapshot, a recent-activity feed, and a live progress chart.

### For admins
- **Admin Dashboard** — total users, total admins, total skills, total career roles,
  most popular skills, and users grouped by target role.
- **User management** — enable/disable accounts, promote/demote roles, delete users.
- **Skill catalog management** — create/edit/delete skills and categories.
- **Career role management** — create/edit/delete roles and attach required skills with
  a required proficiency level.

---

## 3. Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3 (Web, Data JPA, Security, Validation) |
| Build tool | Maven |
| Database | MySQL 8+ |
| ORM | Hibernate (via Spring Data JPA) |
| Frontend | HTML5, CSS3, vanilla JavaScript, Bootstrap 5, Chart.js |
| Auth | Spring Security session/cookie auth, BCrypt |

---

## 4. Project Architecture

```
com.clario.app
├── config          # Spring Security configuration
├── controller       # REST controllers (thin — delegate to services)
├── service           # Business logic, including the skill-gap algorithm
├── repository        # Spring Data JPA repositories
├── entity              # JPA entities
├── dto                  # Request/response DTOs (entities are never exposed directly)
├── exception            # Custom exceptions + a global @RestControllerAdvice handler
└── security              # UserDetailsService, JSON auth success/failure/entry-point handlers
```

The frontend lives under `src/main/resources/static/`:

```
static/
├── index.html            # Landing page
├── css/style.css          # The entire custom design system
├── js/
│   ├── api.js               # fetch() wrapper (adds credentials, parses errors)
│   ├── toast.js               # Toast notifications
│   ├── shell.js                 # Sidebar/topbar shell + auth guard, shared by every page
│   └── <page>.js                  # One script per page
└── pages/
    ├── login.html, register.html
    ├── dashboard.html, skills.html, career-goals.html, skill-gap.html,
    │   progress.html, certifications.html, achievements.html, profile.html, settings.html
    └── admin-dashboard.html, admin-users.html, admin-skills.html,
        admin-categories.html, admin-career-roles.html
```

---

## 5. Database Design

Tables (all created automatically by Hibernate from the JPA entities):

| Table | Purpose |
|---|---|
| `roles` | ROLE_USER / ROLE_ADMIN |
| `users` | Login credentials + role |
| `user_profiles` | One-to-one profile details per user |
| `skill_categories` | Skill groupings (Programming Languages, DevOps, ...) |
| `skills` | Master skill catalog |
| `user_skills` | A user's skills with proficiency (unique per user+skill) |
| `skill_progress_history` | Every proficiency change over time, per user skill |
| `career_roles` | Predefined target roles |
| `career_role_skills` | Required skills + required proficiency per role |
| `user_career_goals` | A user's single selected target role |
| `certifications` | User certifications |
| `achievements` | User achievements |
| `activity_logs` | Recent-activity feed entries |

Relationships use proper foreign keys (`@ManyToOne`/`@OneToMany`/`@OneToOne`), and unique
constraints prevent duplicate skills-per-user, duplicate required-skills-per-role, and a
single career goal per user.

We use Hibernate's `ddl-auto=update` instead of a hand-written `schema.sql` so the schema
always matches the entity model; `data.sql` (idempotent — safe to run on every restart)
seeds realistic demo data.

---

## 6. Setup & Running Locally (VS Code)

### Prerequisites
- Java 17 or later (JDK)
- Maven (or use your IDE's bundled Maven)
- MySQL 8+ running locally
- VS Code with the "Extension Pack for Java" and "Spring Boot Extension Pack"
  (optional but recommended)

### Step 1 — Create the database
You don't need to create tables manually — Hibernate does that — but the database
itself must exist. Either:

```sql
CREATE DATABASE clario_db;
```

...or rely on `createDatabaseIfNotExist=true` in the JDBC URL (already configured), which
works as long as your MySQL user has permission to create databases.

### Step 2 — Configure credentials
`src/main/resources/application.properties` already ships with a default local MySQL
password set (`aryan123`) so it runs out of the box on the machine it was configured for.
If you're running this on a different machine, either edit that file directly, or
(preferred) set environment variables so you never have to touch it:

```bash
export DB_URL="jdbc:mysql://localhost:3306/clario_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true"
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
```

### Step 3 — Run the application
From the project root:

```bash
mvn spring-boot:run
```

Or, build a jar and run it directly:

```bash
mvn clean package
java -jar target/clario.jar
```

In VS Code, you can also just open `ClarioApplication.java` and click "Run".

The app starts on **http://localhost:8080**. On first startup, Hibernate creates the
schema and `data.sql` seeds the demo data described below.

### Step 4 — Open the frontend
Just visit **http://localhost:8080** in your browser — the frontend is served directly
by Spring Boot from `src/main/resources/static/`, so there's no separate frontend server
to run.

---

## 7. Demo Credentials

All seeded accounts share the same password: **`Password123!`**

| Role | Email | Notes |
|---|---|---|
| Admin | `priya.sharma@clario.com` | Full admin access |
| User | `arjun.mehta@clario.com` | Targeting Java Backend Developer, has skill history |
| User | `sarah.chen@clario.com` | Targeting Full Stack Developer |
| User | `michael.rodriguez@clario.com` | Targeting DevOps Engineer |

You can also register a brand-new account from the landing page at any time.

---

## 8. API Overview

All endpoints are under `/api`. Session cookie auth — the frontend sends
`credentials: 'include'` on every request.

| Endpoint | Notes |
|---|---|
| `POST /api/auth/register` | Public |
| `POST /api/auth/login` | Public (handled by Spring Security's form login filter) |
| `POST /api/auth/logout` | Authenticated |
| `GET /api/auth/me` | Current session's user |
| `GET/PUT /api/profile` | Current user's profile |
| `GET /api/skills/categories`, `GET /api/skills/catalog` | Read-only catalog lookups |
| `GET/POST /api/skills`, `PUT/DELETE /api/skills/{id}` | Current user's skills |
| `GET /api/career-roles`, `GET /api/career-roles/{id}` | Public role catalog |
| `GET/PUT /api/career-roles/goal` | Current user's target role |
| `GET /api/skill-gap`, `GET /api/skill-gap/preview/{roleId}` | Skill-gap report |
| `GET /api/progress`, `GET /api/progress/{userSkillId}` | Proficiency history |
| `GET/POST /api/certifications`, `PUT/DELETE /api/certifications/{id}` | Certifications |
| `GET/POST /api/achievements`, `PUT/DELETE /api/achievements/{id}` | Achievements |
| `GET /api/dashboard` | Aggregated dashboard payload |
| `GET /api/admin/stats` | Admin only |
| `GET /api/admin/users`, `PUT/DELETE /api/admin/users/{id}` | Admin only |
| `GET/POST /api/admin/categories`, `DELETE /api/admin/categories/{id}` | Admin only |
| `GET/POST /api/admin/skills`, `PUT/DELETE /api/admin/skills/{id}` | Admin only |
| `GET/POST /api/admin/career-roles`, `PUT/DELETE /api/admin/career-roles/{id}` | Admin only |
| `POST /api/admin/career-roles/{id}/required-skills`, `DELETE .../required-skills/{skillId}` | Admin only |

---

## 9. Main Application Workflows

1. **Register or log in** → land on the Dashboard.
2. **Add skills** on the My Skills page (pick an existing catalog skill or type a new
   one). Each proficiency change is recorded in that skill's progress history.
3. **Set a career goal** on the Career Goals page — pick from the role catalog.
4. **View the Skill Gap Analysis** — see per-skill status (Strong / Needs Improvement /
   Missing) and an overall readiness percentage.
5. **Track certifications and achievements** as you earn them.
6. **Admins** manage the shared catalog (skills, categories, career roles and their
   required skills) and platform users from the Admin section.

---

## 10. Skill-Gap Calculation (Explained)

This is intentionally simple, transparent math — no AI/ML involved — so it can be
explained confidently in a technical interview. See `SkillGapService.java`.

1. Every proficiency level has a numeric weight:
   `BEGINNER = 1`, `INTERMEDIATE = 2`, `ADVANCED = 3`, `EXPERT = 4`.
2. For each skill required by the target role, compare the user's current weight to the
   required weight. A missing skill counts as weight `0`.
3. Each skill's **contribution** = `min(currentWeight / requiredWeight, 1.0)` — exceeding
   the requirement still only counts as 100% for that skill.
4. **Overall readiness %** = the average of every required skill's contribution,
   expressed as a percentage.
5. Each skill is also labeled:
   - **Strong** — current weight ≥ required weight
   - **Needs Improvement** — user has the skill, but below the required level
   - **Missing** — user does not have the skill at all

Example: target role requires Java at Advanced (3). The user is at Intermediate (2).
That skill contributes `2 / 3 ≈ 66.7%` toward the overall readiness score.

---

## 11. Future Enhancement Ideas

- Resume/PDF export of a user's profile, skills, and certifications.
- Email reminders for expiring certifications.
- Skill recommendations based on common skill co-occurrence in the catalog (still
  rule-based, no ML, to preserve transparency).
- Org/team view for managers to see aggregate readiness across their reports.
- Pagination on the admin user list for larger installations.

---

## 12. Notes & What Wasn't Tested

This project was built and reviewed for correctness (schema alignment, DTO mapping,
security rules, and the skill-gap algorithm), but it was **not run end-to-end against a
live MySQL instance** in the environment this was generated in (no outbound network/DB
access was available there). Before relying on it:

- Run `mvn clean package` once to confirm your local Maven/JDK setup resolves all
  dependencies cleanly.
- Start MySQL locally and confirm the app boots and seeds data without errors — watch
  the console for any SQL warnings on first run.
- Spring Boot's Open-Session-In-View is left at its default (`true`), which is what lets
  lazy JPA relationships (like a career role's required skills) load cleanly outside of
  explicit `@Transactional` blocks in a couple of read-only service methods. This is a
  reasonable default for a project this size; a larger production system would typically
  disable OSIV and make repository access explicitly transactional throughout.

---

## 13. Author

**Tanishka Gupta**

---

## 14. License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for
the full text.
