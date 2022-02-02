/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sv.classroster.controllers;

import com.sv.classroster.dao.StudentDao;
import com.sv.classroster.dto.Student;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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

    Set<ConstraintViolation<Student>> violations = new HashSet<>();
    
    @GetMapping("students")
    public String displayStudents(Model model) {
        List<Student> students = studentDao.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("errors", violations);
        return "students";
    }

    @PostMapping("addStudent")
    public String addStudent(String firstName, String lastName) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(student);
        
        if (violations.isEmpty()) {
            studentDao.addStudent(student);
        }
        
        return "redirect:/students";
    }
    
    @GetMapping("deleteStudent")
    public String deleteStudent(int id) {
        studentDao.removeStudentById(id);
        return "redirect:/students";
    }
    
//    @GetMapping("editStudent")
//    public String editStudent(int id, Model model) {
//        Student student = studentDao.getStudentById(id);        
//        model.addAttribute("student", student);
//        return "editStudent";
//    }
    
    @GetMapping("editStudent")
    public String editStudent(int id, Model model) {
        Student student = studentDao.getStudentById(id);
        model.addAttribute("student", student);
        return "editStudent";
    }
    
    @PostMapping("editStudent")
    public String editStudentById(@Valid Student student, BindingResult result) {
        if (result.hasErrors()) {
            return "editStudent";
        }
        studentDao.updateStudent(student);
        return "redirect:/students";
    }
}