DROP DATABASE IF EXISTS classRoster;
CREATE DATABASE classRoster;

USE classRoster;

CREATE TABLE teacher (
	id INT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    specialty VARCHAR(50)
);

CREATE TABLE student (
	id INT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(50) NOT NULL
);

CREATE TABLE course (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    teacherID INT NOT NULL,
    FOREIGN KEY (teacherID) REFERENCES teacher(id)
);

CREATE TABLE course_student (
	courseID INT NOT NULL,
    studentID INT NOT NULL,
    FOREIGN KEY (courseID) REFERENCES course(id),
    FOREIGN KEY (studentID) REFERENCES student(id)
);