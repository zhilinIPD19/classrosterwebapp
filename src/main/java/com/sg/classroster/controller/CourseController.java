package com.sg.classroster.controller;

import com.sg.classroster.dao.CourseDao;
import com.sg.classroster.dao.StudentDao;
import com.sg.classroster.dao.TeacherDao;
import com.sg.classroster.entities.Course;
import com.sg.classroster.entities.Student;
import com.sg.classroster.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class CourseController {

    @Autowired
    TeacherDao teacherDao;

    @Autowired
    StudentDao studentDao;

    @Autowired
    CourseDao courseDao;

    //add a class variable to hold the set of ConstraintViolations from our Validator
    Set<ConstraintViolation<Course>> violations = new HashSet<>();
    Boolean noStudent = false;
    @GetMapping("courses")
    public String displayCourses(Model model) {
        List<Course> courses = courseDao.getAllCourses();
        List<Teacher> teachers = teacherDao.getAllTeachers();
        List<Student> students = studentDao.getAllStudents();
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        model.addAttribute("noStudent",noStudent);
        // violations variable is added to the Model for the main page.
        model.addAttribute("errors", violations);
        return "courses";
    }

    @PostMapping("addCourse")
    public String addCourse(@Valid Course course, BindingResult result, HttpServletRequest request) {
        String teacherId = request.getParameter("teacherId");
        String[] studentIds = request.getParameterValues("studentId");

        course.setTeacher(teacherDao.getTeacherById(Integer.parseInt(teacherId)));

        List<Student> students = new ArrayList<>();
        //If no Students were selected, that field will come in as null and our enhanced for loop will throw an exception, so nee to handle this
        if(studentIds != null) {
            for(String studentId : studentIds) {
                students.add(studentDao.getStudentById(Integer.parseInt(studentId)));
            }
        } else {
            //If it's not null, we proceed like normal; but if it is null, we create a new type of object, a FieldError. The BindingResult uses FieldErrors to keep track of which field has errors in our object.
            noStudent = true;
        }
        course.setStudents(students);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(course);

        if(violations.isEmpty() && !noStudent) {
            courseDao.addCourse(course);
        }
       // if(result.hasErrors()) return "courses";

        return "redirect:/courses";
    }

    @GetMapping("courseDetail")
    public String courseDetail(Integer id, Model model) {
        Course course = courseDao.getCourseById(id);
        model.addAttribute("course", course);
        return "courseDetail";
    }

    @GetMapping("deleteCourse")
    public String deleteCourse(Integer id) {
        courseDao.deleteCourseById(id);
        return "redirect:/courses";
    }

    @GetMapping("editCourse")
    public String editCourse(Integer id, Model model) {
        Course course = courseDao.getCourseById(id);
        List<Student> students = studentDao.getAllStudents();
        List<Teacher> teachers = teacherDao.getAllTeachers();
        model.addAttribute("course", course);
        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);
        return "editCourse";
    }

    /**
     *
     * @param course validating the simple Course fields with the @Valid annotation.
     * @param result add in the BindingResult directly after the Course; the BindingResult must follow whatever we are validating in order to work
     * @param request
     * @param model add in a Model. This page needs extra information to display, the list of Teachers and the list of Students, so we need the Model to get the data back out there if we have validation errors.
     * @return
     */
    @PostMapping("editCourse")
    public String performEditCourse(@Valid Course course, BindingResult result, HttpServletRequest request, Model model) {
        String teacherId = request.getParameter("teacherId");
        String[] studentIds = request.getParameterValues("studentId");

        course.setTeacher(teacherDao.getTeacherById(Integer.parseInt(teacherId)));

        List<Student> students = new ArrayList<>();
        //If no Students were selected, that field will come in as null and our enhanced for loop will throw an exception, so nee to handle this
        if(studentIds != null) {
            for(String studentId : studentIds) {
                students.add(studentDao.getStudentById(Integer.parseInt(studentId)));
            }
        } else {
            //If it's not null, we proceed like normal; but if it is null, we create a new type of object, a FieldError. The BindingResult uses FieldErrors to keep track of which field has errors in our object.
            FieldError error = new FieldError("course", "students", "Must include one student");
            result.addError(error);
        }
        course.setStudents(students);

        //check if our BindingResult has any errors. If it does, we need to put some data into our Model
        if(result.hasErrors()) {
            model.addAttribute("teachers", teacherDao.getAllTeachers());
            model.addAttribute("students", studentDao.getAllStudents());
            model.addAttribute("course", course);
            return "editCourse";
        }
        courseDao.updateCourse(course);

        return "redirect:/courses";
    }
}
