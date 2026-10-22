CREATE DATABASE IF NOT EXISTS student_system;
USE student_system;

CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    semester   INT,
    email      VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS courses (
    course_id   VARCHAR(20) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    credits     INT NOT NULL
);

CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id    VARCHAR(20) NOT NULL,
    course_id     VARCHAR(20) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id)  REFERENCES courses(course_id),
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id)
);

CREATE TABLE IF NOT EXISTS grades (
    grade_id      INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id INT NOT NULL,
    marks         DOUBLE,
    grade         VARCHAR(20),
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id)
);
