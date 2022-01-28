
package com.sv.classroster.dao;

/**
 *
 * @author StevePro
 */
public interface TeacherDao {
    
    Teacher getTeacherById(int id);
    List<Teacher> getAllTeachers();
    Teacher addTeacher(Teacher teacher);
    void updateTeacher(Teacher teacher);
    void delte TeacherById(int id);
    
}
