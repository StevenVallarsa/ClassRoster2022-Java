/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sv.classroster.controllers;

import com.sv.classroster.dao.StudentDao;
import com.sv.classroster.dto.Student;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author: Steven Vallarsa
 *   email: stevenvallarsa@gmail.com
 *    date: 2022-00-00
 * purpose: 
 */
@Controller
public class StudentController {

   @Autowired
   StudentDao studentDao;
   
   @GetMapping("students")
   public String displayStudents(Model model) {
       List<Student> students = studentDao.getAllStudents();
       model.addAttribute("students", students);
       return "students";
   }
   
    @PostMapping("addStudent")
    public String addStudent(String firstName, String lastName) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        studentDao.addStudent(student);
        return "redirect:/students";
    }
    
    @GetMapping("deleteStudent")
    public String deleteStudent(int id) {
        studentDao.removeStudentById(id);
        return "redirect:/students";
    }
    
    @GetMapping("editStudent")
    public String editStudent(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Student student = studentDao.getStudentById(id);
        model.addAttribute("student", student);
        return "editStudent";
    }
    
    @PostMapping("editStudent")
    public String editStudentById(Student student) {
        studentDao.updateStudent(student);
        return "redirect:/students";
    }
}