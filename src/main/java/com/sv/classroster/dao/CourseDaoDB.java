
package com.sv.classroster.dao;

import com.sv.classroster.dao.StudentDaoDB.StudentMapper;
import com.sv.classroster.dao.TeacherDaoDB.TeacherMapper;
import com.sv.classroster.dto.Course;
import com.sv.classroster.dto.Student;
import com.sv.classroster.dto.Teacher;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author: Steven Vallarsa
 *   email: stevenvallarsa@gmail.com
 *    date: 2022-01-28
 * purpose: 
 */
@Repository
public class CourseDaoDB implements CourseDao {
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Course getCourseById(int id) {
        final String SELECT_COURSE_BY_ID = "SELECT * FROM course WHERE id = ?";
        try {
            Course course = jdbc.queryForObject(SELECT_COURSE_BY_ID, new CourseMapper(), id);
            course.setTeacher(getTeacherForCourse(id));
            course.setStudents(getStudentsForCourse(id));
            return course;
            
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Teacher getTeacherForCourse(int id) {
        final String SELECT_TEACHER_FOR_COURSE = "SELECT t.* FROM teacher t JOIN course c ON t.id = c.teacherID WHERE c.id = ?";
        return jdbc.queryForObject(SELECT_TEACHER_FOR_COURSE, new TeacherMapper(), id);
    }
    
    private List<Student> getStudentsForCourse(int id) {
        final String SELECT_STUDENTS_FOR_COURSE = "SELECT s.* FROM student s JOIN course_student cs ON s.id = cs.studentID WHERE cs.courseID = ?";
        return jdbc.query(SELECT_STUDENTS_FOR_COURSE, new StudentMapper(), id);
    }
    
    @Override
    public List<Course> getAllCourses() {
        final String SELECT_ALL_COURSES = "SELECT * FROM course";
        List<Course> courses = jdbc.query(SELECT_ALL_COURSES, new CourseMapper());
        associateTeacherAndStudents(courses);
        return courses;
    }
    
    private void associateTeacherAndStudents(List<Course> courses) {
        for (Course course : courses) {
            course.setTeacher(getTeacherForCourse(course.getId()));
            course.setStudents(getStudentsForCourse(course.getId()));
        }
    }

    @Override
    @Transactional
    public Course addCourse(Course course) {
        final String INSERT_COURSE = "INSERT INTO course (name, description, teacherID) VALUES(?,?,?)";
        jdbc.update(INSERT_COURSE,
                course.getName(),
                course.getDescription(),
                course.getTeacher().getId());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        course.setId(newId);
        insertCourseStudent(course);
        return course;
    }
    
    private void insertCourseStudent(Course course) {
        final String INSERT_COURSE_STUDENT = "INSERT INTO course_student (courseID, studentID) VALUES (?, ?)";
        for (Student student : course.getStudents()) {
            jdbc.update(INSERT_COURSE_STUDENT, 
                    course.getId(), 
                    student.getId());
        }
    }

    @Override
    @Transactional
    public void updateCourse(Course course) {
        final String UPDATE_COURSE = "UPDATE course SET name = ?, description = ?, teacherID = ? WHERE id = ?";
        jdbc.update(UPDATE_COURSE,
                course.getName(),
                course.getDescription(),
                course.getTeacher().getId(),
                course.getId());
        
        final String DELETE_COURSE_STUDENT = "DELETE FROM course_student WHERE courseID = ?";
        jdbc.update(DELETE_COURSE_STUDENT, course.getId());
        insertCourseStudent(course);
        
    }

    @Override
    @Transactional
    public void deleteCourseById(int id) {
        final String DELETE_COURSE_STUDENT = "DELETE FROM course_student WHERE courseID = ?";
        jdbc.update(DELETE_COURSE_STUDENT, id);
        
        final String DELETE_COURSE_BY_ID = "DELETE FROM course WHERE id = ?";
        jdbc.update(DELETE_COURSE_BY_ID, id);
        
        
    }

    @Override
    public List<Course> getCoursesForTeacher(Teacher teacher) {
        final String SELECT_COURSES_FOR_TEACHER = "SELECT * FROM course WHERE teacheriD = ?";
        List<Course> courses = jdbc.query(SELECT_COURSES_FOR_TEACHER, new CourseMapper(), teacher.getId());
        associateTeacherAndStudents(courses);
        return courses;
    }

    @Override
    public List<Course> getCoursesForStudent(Student student) {
        final String SELECT_COURSES_FOR_STUDENT = "SELECT c.* FROM course c JOIN course_student cs ON c.id == cs.courseID WHERE cs.studentID = ?";
        List<Course> courses = jdbc.query(SELECT_COURSES_FOR_STUDENT, new CourseMapper(), student.getId());
        associateTeacherAndStudents(courses);
        return courses;
    }

    
    public static final class CourseMapper implements RowMapper<Course> {

        @Override
        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course();
            course.setId(rs.getInt("id"));
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            return course;
        }
        
    }
    
    
}
