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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
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
public class CourseController {

   @Autowired
   TeacherDao teacherDao;

   @Autowired
   StudentDao studentDao;

   @Autowired
   CourseDao courseDao;
   
   @GetMapping("courses")
   public String displayAllCourses(Model model) {
       List<Course> courses = courseDao.getAllCourses();
       List<Teacher> teachers = teacherDao.getAllTeachers();
       List<Student> students = studentDao.getAllStudents();
       model.addAttribute("courses", courses);
       model.addAttribute("teachers", teachers);
       model.addAttribute("students", students);
       return "courses";
   }
   
   @PostMapping("addCourse")
   public String addCourse(Course course, HttpServletRequest request) {
       int teacherID = Integer.parseInt(request.getParameter("teacherId"));
       String[] studentIDs = request.getParameterValues("studentId");
       course.setTeacher(teacherDao.getTeacherById(teacherID));
       
       List<Student> students = new ArrayList<>();
       for (String studentID : studentIDs) {
           students.add(studentDao.getStudentById(Integer.parseInt(studentID)));
       }
       course.setStudents(students);
       courseDao.addCourse(course);
       
       return "redirect:/courses";
   }
}
