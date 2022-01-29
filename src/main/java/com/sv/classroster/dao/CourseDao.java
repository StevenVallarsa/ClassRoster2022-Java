
package com.sv.classroster.dao;

import com.sv.classroster.dto.Course;
import com.sv.classroster.dto.Student;
import com.sv.classroster.dto.Teacher;
import java.util.List;

/**
 *
 * @author StevePro
 */
public interface CourseDao {
    
    Course getCourseById(int id);
    List<Course> getAllCourses();
    Course addCourse(Course course);
    void updateCourse(Course course);
    void deleteCourseById(int id);
    
    List<Course> getCoursesForTeacher(Teacher teacher);
    List<Course> getCoursesForStudent(Student student);
    
}
