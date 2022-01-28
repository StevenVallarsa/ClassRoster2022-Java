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

CREATE table course (
	id int primary key AUTO_INCREMENT,
    name varchar(50) not null,
    description varchar(255),
    teacherID int not null,
    FOREIGN KEY (teacherID) REFERENCES teacher(id)
);

Create TABLE course_student (
	courseID int not null,
    studentID int not null,
    FOREIGN KEY (courseID) REFERENCES course(id),
    FOREIGN KEY (studentID) REFERENCES student(id)
);