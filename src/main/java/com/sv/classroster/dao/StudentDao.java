
package com.sv.classroster.dao;

/**
 *
 * @author StevePro
 */
public interface StudentDao {
    
    Student getStudentById(int id);
    List<Student> getAllStudents();
    Student addStudent(Student student);
    void updateStudent(Student student);
    void removeStudentById(int id);
    
}
