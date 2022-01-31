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
   public String editCourseDetails(Course course, HttpServletRequest request) {
       String teacherID = request.getParameter("teacherId");
       String[] studentIDs = request.getParameterValues("studentId");
       
       course.setTeacher(teacherDao.getTeacherById(Integer.parseInt(teacherID)));
       
       List<Student> students = new ArrayList<>();
       
       for (String studentID : studentIDs) {
           students.add(studentDao.getStudentById(Integer.parseInt(studentID)));
       }
       
       course.setStudents(students);
       courseDao.updateCourse(course);
       
       return "redirect:/courses";
   }
   
   
}
