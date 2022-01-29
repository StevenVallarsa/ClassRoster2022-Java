
package com.sv.classroster.dao;

import com.sv.classroster.dto.Teacher;
import java.util.List;

/**
 *
 * @author StevePro
 */
public interface TeacherDao {
    
    Teacher getTeacherById(int id);
    List<Teacher> getAllTeachers();
    Teacher addTeacher(Teacher teacher);
    void updateTeacher(Teacher teacher);
    void deleteTeacherById(int id);
    
}
