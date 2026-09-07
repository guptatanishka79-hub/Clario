-- ============================================================================
-- Clario seed data
-- All inserts are guarded (INSERT IGNORE / WHERE NOT EXISTS) so this script
-- is safe to run every time the application starts (spring.sql.init.mode=always).
-- Demo password for every seeded account: Password123!
-- (bcrypt hash below was generated at cost factor 10)
-- ============================================================================

-- ---------- Roles ----------
INSERT IGNORE INTO roles (name) VALUES ('ROLE_USER');
INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN');

-- ---------- Skill categories ----------
INSERT IGNORE INTO skill_categories (name) VALUES ('Programming Languages');
INSERT IGNORE INTO skill_categories (name) VALUES ('Backend Development');
INSERT IGNORE INTO skill_categories (name) VALUES ('Frontend Development');
INSERT IGNORE INTO skill_categories (name) VALUES ('Databases');
INSERT IGNORE INTO skill_categories (name) VALUES ('DevOps');
INSERT IGNORE INTO skill_categories (name) VALUES ('Cloud');
INSERT IGNORE INTO skill_categories (name) VALUES ('Testing');
INSERT IGNORE INTO skill_categories (name) VALUES ('Tools');
INSERT IGNORE INTO skill_categories (name) VALUES ('Soft Skills');

-- ---------- Skills ----------
INSERT IGNORE INTO skills (name, category_id) SELECT 'Java', id FROM skill_categories WHERE name = 'Programming Languages';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Python', id FROM skill_categories WHERE name = 'Programming Languages';
INSERT IGNORE INTO skills (name, category_id) SELECT 'JavaScript', id FROM skill_categories WHERE name = 'Programming Languages';
INSERT IGNORE INTO skills (name, category_id) SELECT 'SQL', id FROM skill_categories WHERE name = 'Databases';
INSERT IGNORE INTO skills (name, category_id) SELECT 'MySQL', id FROM skill_categories WHERE name = 'Databases';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Spring Boot', id FROM skill_categories WHERE name = 'Backend Development';
INSERT IGNORE INTO skills (name, category_id) SELECT 'REST APIs', id FROM skill_categories WHERE name = 'Backend Development';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Node.js', id FROM skill_categories WHERE name = 'Backend Development';
INSERT IGNORE INTO skills (name, category_id) SELECT 'React', id FROM skill_categories WHERE name = 'Frontend Development';
INSERT IGNORE INTO skills (name, category_id) SELECT 'HTML/CSS', id FROM skill_categories WHERE name = 'Frontend Development';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Git', id FROM skill_categories WHERE name = 'Tools';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Linux', id FROM skill_categories WHERE name = 'Tools';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Excel', id FROM skill_categories WHERE name = 'Tools';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Tableau', id FROM skill_categories WHERE name = 'Tools';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Docker', id FROM skill_categories WHERE name = 'DevOps';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Kubernetes', id FROM skill_categories WHERE name = 'DevOps';
INSERT IGNORE INTO skills (name, category_id) SELECT 'CI/CD', id FROM skill_categories WHERE name = 'DevOps';
INSERT IGNORE INTO skills (name, category_id) SELECT 'AWS', id FROM skill_categories WHERE name = 'Cloud';
INSERT IGNORE INTO skills (name, category_id) SELECT 'JUnit', id FROM skill_categories WHERE name = 'Testing';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Selenium', id FROM skill_categories WHERE name = 'Testing';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Test Automation', id FROM skill_categories WHERE name = 'Testing';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Communication', id FROM skill_categories WHERE name = 'Soft Skills';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Problem Solving', id FROM skill_categories WHERE name = 'Soft Skills';
INSERT IGNORE INTO skills (name, category_id) SELECT 'Agile/Scrum', id FROM skill_categories WHERE name = 'Soft Skills';

-- ---------- Career roles ----------
INSERT IGNORE INTO career_roles (name, description) VALUES
  ('Java Backend Developer', 'Designs and builds server-side applications and APIs using Java and the Spring ecosystem.');
INSERT IGNORE INTO career_roles (name, description) VALUES
  ('Full Stack Developer', 'Builds both the client-facing and server-side portions of web applications.');
INSERT IGNORE INTO career_roles (name, description) VALUES
  ('Software Engineer', 'General-purpose engineering role spanning application design, coding, and problem-solving.');
INSERT IGNORE INTO career_roles (name, description) VALUES
  ('Data Analyst', 'Extracts insight from data using SQL, Python, and visualization tools to support decisions.');
INSERT IGNORE INTO career_roles (name, description) VALUES
  ('DevOps Engineer', 'Owns build, release, and infrastructure automation across the delivery pipeline.');
INSERT IGNORE INTO career_roles (name, description) VALUES
  ('QA Engineer', 'Designs and executes manual and automated tests to ensure product quality.');

-- ---------- Required skills per career role ----------
-- Java Backend Developer
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'Java Backend Developer' AND s.name = 'Java';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Java Backend Developer' AND s.name = 'Spring Boot';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Java Backend Developer' AND s.name = 'SQL';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Java Backend Developer' AND s.name = 'REST APIs';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Java Backend Developer' AND s.name = 'Git';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'BEGINNER' FROM career_roles cr, skills s WHERE cr.name = 'Java Backend Developer' AND s.name = 'Docker';

-- Full Stack Developer
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Full Stack Developer' AND s.name = 'JavaScript';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'Full Stack Developer' AND s.name = 'HTML/CSS';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Full Stack Developer' AND s.name = 'React';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Full Stack Developer' AND s.name = 'Node.js';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Full Stack Developer' AND s.name = 'SQL';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Full Stack Developer' AND s.name = 'Git';

-- Software Engineer
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Software Engineer' AND s.name = 'Java';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Software Engineer' AND s.name = 'Python';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Software Engineer' AND s.name = 'SQL';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Software Engineer' AND s.name = 'Git';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'Software Engineer' AND s.name = 'Problem Solving';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'BEGINNER' FROM career_roles cr, skills s WHERE cr.name = 'Software Engineer' AND s.name = 'REST APIs';

-- Data Analyst
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'Data Analyst' AND s.name = 'SQL';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Data Analyst' AND s.name = 'Python';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'Data Analyst' AND s.name = 'Excel';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Data Analyst' AND s.name = 'Tableau';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'Data Analyst' AND s.name = 'Communication';

-- DevOps Engineer
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'DevOps Engineer' AND s.name = 'Docker';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'DevOps Engineer' AND s.name = 'Kubernetes';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'DevOps Engineer' AND s.name = 'AWS';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'DevOps Engineer' AND s.name = 'Linux';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'DevOps Engineer' AND s.name = 'Git';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'DevOps Engineer' AND s.name = 'CI/CD';

-- QA Engineer
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'QA Engineer' AND s.name = 'JUnit';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'QA Engineer' AND s.name = 'Selenium';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'ADVANCED' FROM career_roles cr, skills s WHERE cr.name = 'QA Engineer' AND s.name = 'Test Automation';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'QA Engineer' AND s.name = 'SQL';
INSERT IGNORE INTO career_role_skills (career_role_id, skill_id, required_proficiency)
SELECT cr.id, s.id, 'INTERMEDIATE' FROM career_roles cr, skills s WHERE cr.name = 'QA Engineer' AND s.name = 'Communication';

-- ============================================================================
-- Demo accounts
-- Password for every account below: Password123!
-- ============================================================================

-- Admin: Priya Sharma
INSERT IGNORE INTO users (full_name, email, password, role_id, enabled, created_at)
SELECT 'Priya Sharma', 'priya.sharma@clario.com',
       '$2b$10$VDLMn.6elfyYH8UZTrSjfeNCKIj.40vDPcxTUVm3W2kjpaHXIrfee',
       r.id, true, NOW()
FROM roles r WHERE r.name = 'ROLE_ADMIN';

INSERT IGNORE INTO user_profiles (user_id, phone, location, headline, bio, years_of_experience, current_job_title)
SELECT u.id, '+91 98765 43210', 'Bengaluru, India', 'Platform Administrator, Clario',
       'Manages the Clario platform catalog, career-role definitions, and user administration.',
       6, 'Engineering Manager'
FROM users u WHERE u.email = 'priya.sharma@clario.com'
AND NOT EXISTS (SELECT 1 FROM user_profiles up WHERE up.user_id = u.id);

-- Demo user 1: Arjun Mehta (targeting Java Backend Developer)
INSERT IGNORE INTO users (full_name, email, password, role_id, enabled, created_at)
SELECT 'Arjun Mehta', 'arjun.mehta@clario.com',
       '$2b$10$VDLMn.6elfyYH8UZTrSjfeNCKIj.40vDPcxTUVm3W2kjpaHXIrfee',
       r.id, true, NOW()
FROM roles r WHERE r.name = 'ROLE_USER';

INSERT IGNORE INTO user_profiles (user_id, phone, location, headline, bio, years_of_experience, current_job_title)
SELECT u.id, '+91 90000 11122', 'Bengaluru, India', 'Backend Developer aiming for Senior Java roles',
       'Three years building internal tools and APIs; now deepening Spring Boot and cloud fundamentals.',
       3, 'Software Engineer II'
FROM users u WHERE u.email = 'arjun.mehta@clario.com'
AND NOT EXISTS (SELECT 1 FROM user_profiles up WHERE up.user_id = u.id);

-- Demo user 2: Sarah Chen (targeting Full Stack Developer)
INSERT IGNORE INTO users (full_name, email, password, role_id, enabled, created_at)
SELECT 'Sarah Chen', 'sarah.chen@clario.com',
       '$2b$10$VDLMn.6elfyYH8UZTrSjfeNCKIj.40vDPcxTUVm3W2kjpaHXIrfee',
       r.id, true, NOW()
FROM roles r WHERE r.name = 'ROLE_USER';

INSERT IGNORE INTO user_profiles (user_id, phone, location, headline, bio, years_of_experience, current_job_title)
SELECT u.id, '+1 415 555 0138', 'San Francisco, CA', 'Aspiring Full Stack Developer',
       'Self-taught developer with one year of professional experience, focused on modern JavaScript and React.',
       1, 'Junior Developer'
FROM users u WHERE u.email = 'sarah.chen@clario.com'
AND NOT EXISTS (SELECT 1 FROM user_profiles up WHERE up.user_id = u.id);

-- Demo user 3: Michael Rodriguez (targeting DevOps Engineer)
INSERT IGNORE INTO users (full_name, email, password, role_id, enabled, created_at)
SELECT 'Michael Rodriguez', 'michael.rodriguez@clario.com',
       '$2b$10$VDLMn.6elfyYH8UZTrSjfeNCKIj.40vDPcxTUVm3W2kjpaHXIrfee',
       r.id, true, NOW()
FROM roles r WHERE r.name = 'ROLE_USER';

INSERT IGNORE INTO user_profiles (user_id, phone, location, headline, bio, years_of_experience, current_job_title)
SELECT u.id, '+1 512 555 0199', 'Austin, TX', 'Systems Administrator moving into DevOps',
       'Five years managing on-prem Linux infrastructure; now building automation and cloud skills.',
       5, 'Systems Administrator'
FROM users u WHERE u.email = 'michael.rodriguez@clario.com'
AND NOT EXISTS (SELECT 1 FROM user_profiles up WHERE up.user_id = u.id);

-- ============================================================================
-- Skills, career goals, certifications, achievements for demo users
-- ============================================================================

-- ---------- Arjun Mehta's skills (targeting Java Backend Developer) ----------
INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'ADVANCED', 3.0, CURDATE() FROM users u, skills s
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'Java';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'INTERMEDIATE', 2.0, CURDATE() FROM users u, skills s
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'SQL';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'INTERMEDIATE', 2.5, CURDATE() FROM users u, skills s
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'Git';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'INTERMEDIATE', 2.0, CURDATE() FROM users u, skills s
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'REST APIs';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'BEGINNER', 0.5, CURDATE() FROM users u, skills s
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'Docker';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'INTERMEDIATE', 3.0, CURDATE() FROM users u, skills s
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'Communication';

-- Java proficiency history for Arjun (Beginner -> Intermediate -> Advanced over the year)
INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'BEGINNER', DATE_SUB(CURDATE(), INTERVAL 7 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'Java'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'BEGINNER'
);

INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'INTERMEDIATE', DATE_SUB(CURDATE(), INTERVAL 5 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'Java'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'INTERMEDIATE'
);

INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'ADVANCED', DATE_SUB(CURDATE(), INTERVAL 2 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'Java'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'ADVANCED'
);

-- SQL history for Arjun
INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'BEGINNER', DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'SQL'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'BEGINNER'
);

INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'INTERMEDIATE', DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'arjun.mehta@clario.com' AND s.name = 'SQL'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'INTERMEDIATE'
);

-- Career goal: Arjun -> Java Backend Developer
INSERT IGNORE INTO user_career_goals (user_id, career_role_id, set_date)
SELECT u.id, cr.id, DATE_SUB(CURDATE(), INTERVAL 4 MONTH)
FROM users u, career_roles cr
WHERE u.email = 'arjun.mehta@clario.com' AND cr.name = 'Java Backend Developer';

-- Arjun's certification and achievement
INSERT INTO certifications (user_id, name, issuing_organization, issue_date, expiry_date, credential_id, credential_url)
SELECT u.id, 'Oracle Certified Professional: Java SE 11 Developer', 'Oracle',
       DATE_SUB(CURDATE(), INTERVAL 8 MONTH), DATE_ADD(CURDATE(), INTERVAL 40 MONTH),
       'OCP-JSE11-88213', 'https://education.oracle.com/certification'
FROM users u WHERE u.email = 'arjun.mehta@clario.com'
AND NOT EXISTS (SELECT 1 FROM certifications c WHERE c.user_id = u.id AND c.name = 'Oracle Certified Professional: Java SE 11 Developer');

INSERT INTO achievements (user_id, title, description, achievement_date, organization)
SELECT u.id, 'Led migration of a legacy monolith to microservices',
       'Coordinated a four-person team to break apart a monolithic billing service into three independently deployable Spring Boot services, cutting release time from weeks to days.',
       DATE_SUB(CURDATE(), INTERVAL 3 MONTH), 'Current employer'
FROM users u WHERE u.email = 'arjun.mehta@clario.com'
AND NOT EXISTS (SELECT 1 FROM achievements a WHERE a.user_id = u.id AND a.title = 'Led migration of a legacy monolith to microservices');

-- ---------- Sarah Chen's skills (targeting Full Stack Developer) ----------
INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'INTERMEDIATE', 1.5, CURDATE() FROM users u, skills s
WHERE u.email = 'sarah.chen@clario.com' AND s.name = 'JavaScript';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'ADVANCED', 1.5, CURDATE() FROM users u, skills s
WHERE u.email = 'sarah.chen@clario.com' AND s.name = 'HTML/CSS';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'BEGINNER', 0.5, CURDATE() FROM users u, skills s
WHERE u.email = 'sarah.chen@clario.com' AND s.name = 'React';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'BEGINNER', 1.0, CURDATE() FROM users u, skills s
WHERE u.email = 'sarah.chen@clario.com' AND s.name = 'Git';

INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'BEGINNER', DATE_SUB(CURDATE(), INTERVAL 5 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'sarah.chen@clario.com' AND s.name = 'JavaScript'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'BEGINNER'
);

INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'INTERMEDIATE', DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'sarah.chen@clario.com' AND s.name = 'JavaScript'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'INTERMEDIATE'
);

INSERT IGNORE INTO user_career_goals (user_id, career_role_id, set_date)
SELECT u.id, cr.id, DATE_SUB(CURDATE(), INTERVAL 2 MONTH)
FROM users u, career_roles cr
WHERE u.email = 'sarah.chen@clario.com' AND cr.name = 'Full Stack Developer';

INSERT INTO certifications (user_id, name, issuing_organization, issue_date, expiry_date, credential_id, credential_url)
SELECT u.id, 'Responsive Web Design', 'freeCodeCamp',
       DATE_SUB(CURDATE(), INTERVAL 6 MONTH), NULL,
       'FCC-RWD-40217', 'https://freecodecamp.org/certification'
FROM users u WHERE u.email = 'sarah.chen@clario.com'
AND NOT EXISTS (SELECT 1 FROM certifications c WHERE c.user_id = u.id AND c.name = 'Responsive Web Design');

INSERT INTO achievements (user_id, title, description, achievement_date, organization)
SELECT u.id, 'Shipped a personal portfolio site',
       'Designed, built, and deployed a responsive portfolio site that now receives over 500 visits a month.',
       DATE_SUB(CURDATE(), INTERVAL 4 MONTH), 'Personal project'
FROM users u WHERE u.email = 'sarah.chen@clario.com'
AND NOT EXISTS (SELECT 1 FROM achievements a WHERE a.user_id = u.id AND a.title = 'Shipped a personal portfolio site');

-- ---------- Michael Rodriguez's skills (targeting DevOps Engineer) ----------
INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'INTERMEDIATE', 2.0, CURDATE() FROM users u, skills s
WHERE u.email = 'michael.rodriguez@clario.com' AND s.name = 'Docker';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'ADVANCED', 5.0, CURDATE() FROM users u, skills s
WHERE u.email = 'michael.rodriguez@clario.com' AND s.name = 'Linux';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'BEGINNER', 0.5, CURDATE() FROM users u, skills s
WHERE u.email = 'michael.rodriguez@clario.com' AND s.name = 'AWS';

INSERT IGNORE INTO user_skills (user_id, skill_id, proficiency_level, years_of_experience, last_updated)
SELECT u.id, s.id, 'INTERMEDIATE', 4.0, CURDATE() FROM users u, skills s
WHERE u.email = 'michael.rodriguez@clario.com' AND s.name = 'Git';

INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'BEGINNER', DATE_SUB(CURDATE(), INTERVAL 10 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'michael.rodriguez@clario.com' AND s.name = 'Docker'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'BEGINNER'
);

INSERT INTO skill_progress_history (user_skill_id, proficiency_level, recorded_date)
SELECT us.id, 'INTERMEDIATE', DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
FROM user_skills us JOIN users u ON us.user_id = u.id JOIN skills s ON us.skill_id = s.id
WHERE u.email = 'michael.rodriguez@clario.com' AND s.name = 'Docker'
AND NOT EXISTS (
  SELECT 1 FROM skill_progress_history h WHERE h.user_skill_id = us.id AND h.proficiency_level = 'INTERMEDIATE'
);

INSERT IGNORE INTO user_career_goals (user_id, career_role_id, set_date)
SELECT u.id, cr.id, DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
FROM users u, career_roles cr
WHERE u.email = 'michael.rodriguez@clario.com' AND cr.name = 'DevOps Engineer';

INSERT INTO certifications (user_id, name, issuing_organization, issue_date, expiry_date, credential_id, credential_url)
SELECT u.id, 'AWS Certified Cloud Practitioner', 'Amazon Web Services',
       DATE_SUB(CURDATE(), INTERVAL 5 MONTH), DATE_ADD(CURDATE(), INTERVAL 31 MONTH),
       'AWS-CCP-950214', 'https://aws.amazon.com/certification'
FROM users u WHERE u.email = 'michael.rodriguez@clario.com'
AND NOT EXISTS (SELECT 1 FROM certifications c WHERE c.user_id = u.id AND c.name = 'AWS Certified Cloud Practitioner');

INSERT INTO achievements (user_id, title, description, achievement_date, organization)
SELECT u.id, 'Cut deployment time by 40%',
       'Wrote a set of shell and Ansible automation scripts that reduced manual server provisioning steps from two hours to under twenty minutes.',
       DATE_SUB(CURDATE(), INTERVAL 6 MONTH), 'Current employer'
FROM users u WHERE u.email = 'michael.rodriguez@clario.com'
AND NOT EXISTS (SELECT 1 FROM achievements a WHERE a.user_id = u.id AND a.title = 'Cut deployment time by 40%');

-- ---------- Recent activity log entries (so the dashboard has data on first login) ----------
INSERT INTO activity_logs (user_id, description, created_at)
SELECT u.id, 'Updated Java proficiency to Advanced', DATE_SUB(NOW(), INTERVAL 60 DAY)
FROM users u WHERE u.email = 'arjun.mehta@clario.com'
AND NOT EXISTS (SELECT 1 FROM activity_logs a WHERE a.user_id = u.id AND a.description = 'Updated Java proficiency to Advanced');

INSERT INTO activity_logs (user_id, description, created_at)
SELECT u.id, 'Added Oracle Certified Professional: Java SE 11 Developer certification', DATE_SUB(NOW(), INTERVAL 30 DAY)
FROM users u WHERE u.email = 'arjun.mehta@clario.com'
AND NOT EXISTS (SELECT 1 FROM activity_logs a WHERE a.user_id = u.id AND a.description = 'Added Oracle Certified Professional: Java SE 11 Developer certification');

INSERT INTO activity_logs (user_id, description, created_at)
SELECT u.id, 'Set career goal to Java Backend Developer', DATE_SUB(NOW(), INTERVAL 90 DAY)
FROM users u WHERE u.email = 'arjun.mehta@clario.com'
AND NOT EXISTS (SELECT 1 FROM activity_logs a WHERE a.user_id = u.id AND a.description = 'Set career goal to Java Backend Developer');

INSERT INTO activity_logs (user_id, description, created_at)
SELECT u.id, 'Added Spring Boot skill', DATE_SUB(NOW(), INTERVAL 10 DAY)
FROM users u WHERE u.email = 'arjun.mehta@clario.com'
AND NOT EXISTS (SELECT 1 FROM activity_logs a WHERE a.user_id = u.id AND a.description = 'Added Spring Boot skill');

INSERT INTO activity_logs (user_id, description, created_at)
SELECT u.id, 'Updated JavaScript proficiency to Intermediate', DATE_SUB(NOW(), INTERVAL 20 DAY)
FROM users u WHERE u.email = 'sarah.chen@clario.com'
AND NOT EXISTS (SELECT 1 FROM activity_logs a WHERE a.user_id = u.id AND a.description = 'Updated JavaScript proficiency to Intermediate');

INSERT INTO activity_logs (user_id, description, created_at)
SELECT u.id, 'Added AWS certification', DATE_SUB(NOW(), INTERVAL 45 DAY)
FROM users u WHERE u.email = 'michael.rodriguez@clario.com'
AND NOT EXISTS (SELECT 1 FROM activity_logs a WHERE a.user_id = u.id AND a.description = 'Added AWS certification');
