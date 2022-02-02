/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sv.classroster.controllers;

import com.sv.classroster.dao.CourseDao;
import com.sv.classroster.dao.StudentDao;
import com.sv.classroster.dao.TeacherDao;
import com.sv.classroster.dto.Course;
import com.sv.classroster.dto.Student;
import com.sv.classroster.dto.Teacher;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class CourseController {

   @Autowired
   TeacherDao teacherDao;

   @Autowired
   StudentDao studentDao;

   @Autowired
   CourseDao courseDao;

    Set<ConstraintViolation<Course>> violations = new HashSet<>();
    
   
   @GetMapping("courses")
   public String displayAllCourses(@Valid Course course, BindingResult result, Model model) {
       List<Course> courses = courseDao.getAllCourses();
       List<Teacher> teachers = teacherDao.getAllTeachers();
       List<Student> students = studentDao.getAllStudents();
       model.addAttribute("courses", courses);
       model.addAttribute("teachers", teachers);
       model.addAttribute("students", students);
       model.addAttribute("err", false);
       return "courses";
   }
   
    @PostMapping("addCourse")
    public String addCourse(@Valid Course course, BindingResult result, HttpServletRequest request, Model model) {
        int teacherID = Integer.parseInt(request.getParameter("teacherId"));
        String[] studentIDs = request.getParameterValues("studentId");
        course.setTeacher(teacherDao.getTeacherById(teacherID));

        List<Student> students = new ArrayList<>();
        if (studentIDs != null) {
            for (String studentID : studentIDs) {
                students.add(studentDao.getStudentById(Integer.parseInt(studentID)));
            }
        } else {
            FieldError error = new FieldError("course", "students", "Must include one student");
            result.addError(error);
        }

        course.setStudents(students);
        
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherDao.getAllTeachers());
            model.addAttribute("students", studentDao.getAllStudents());
            model.addAttribute("courses", courseDao.getAllCourses());
            model.addAttribute("err", true);
            return "courses";
        }
        courseDao.addCourse(course);

        return "redirect:/courses";
    }
   
   @GetMapping("courseDetails")
   public String courseDetail(int id, Model model) {
       Course course = courseDao.getCourseById(id);
       model.addAttribute("course", course);
       return "courseDetails";
   }
   
   @GetMapping("deleteCourse")
   public String deleteCourse(int id) {
       courseDao.deleteCourseById(id);
       return "redirect:/courses";
   }
   
   @GetMapping("editCourse")
   public String editCourse(int id, Model model) {
       Course course = courseDao.getCourseById(id);
       List<Student> students = studentDao.getAllStudents();
       List<Teacher> teachers = teacherDao.getAllTeachers();
       model.addAttribute("course", course);
       model.addAttribute("students", students);
       model.addAttribute("teachers", teachers);
       return "editCourse";
   }
   
   @PostMapping("editCourse")
   public String editCourseDetails(@Valid Course course, BindingResult result, HttpServletRequest request, Model model) {
        int teacherID = Integer.parseInt(request.getParameter("teacherId"));
        String[] studentIDs = request.getParameterValues("studentId");
        course.setTeacher(teacherDao.getTeacherById(teacherID));

        List<Student> students = new ArrayList<>();
        if(studentIDs != null) {
            for (String studentID : studentIDs) {
                students.add(studentDao.getStudentById(Integer.parseInt(studentID)));
            }
        } else {
            FieldError error = new FieldError("course", "students", "Must include one student");
            result.addError(error);
        }
       
       course.setStudents(students);
       
       if (result.hasErrors()) {
           model.addAttribute("teachers", teacherDao.getAllTeachers());
           model.addAttribute("students", studentDao.getAllStudents());
           model.addAttribute("course", course);
           return "editCourse";
       }
       
       courseDao.updateCourse(course);
       
       return "redirect:/courses";
   }
   
}
