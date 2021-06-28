package com.sg.classroster.controller;

import com.sg.classroster.dao.CourseDao;
import com.sg.classroster.dao.StudentDao;
import com.sg.classroster.dao.TeacherDao;
import com.sg.classroster.entities.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class TeacherController {

    @Autowired
    TeacherDao teacherDao;

    @Autowired
    StudentDao studentDao;

    @Autowired
    CourseDao courseDao;

    Set<ConstraintViolation<Teacher>> violations = new HashSet<>();
    //in order to clear the error message when refresh the page
    Boolean isDefault = true;

    @GetMapping("teachers")
    public String displayTeachers(Model model){
        List<Teacher> teacherList = teacherDao.getAllTeachers();
        model.addAttribute("teachers",teacherList);
        //if it is refresh pages, need to clear the error messages ahead
        if(isDefault){
            violations.clear();
        }
        // violations variable is added to the Model for the main page.
        model.addAttribute("errors", violations);
        //always reset isDefault to true after error message showed
        isDefault = true;
        return "teachers";
    }

    @PostMapping("addTeacher")
    public String addTeacher(HttpServletRequest request){
        isDefault = false;
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String specialty = request.getParameter("specialty");

        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setSpecialty(specialty);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(teacher);

        if(violations.isEmpty()) {
            teacherDao.addTeacher(teacher);
        }
        return "redirect:/teachers";
    }

    @GetMapping("editTeacher")
    public String editTeacher(HttpServletRequest request, Model model){
        int id = Integer.parseInt(request.getParameter("id"));
        Teacher teacher = teacherDao.getTeacherById(id);

        model.addAttribute("teacher", teacher);
        if(isDefault){
            violations.clear();
        }
        model.addAttribute("errors", violations);
        isDefault = true;
        return "editTeacher";
    }

    @PostMapping("editTeacher")
    public String performEditTeacher(@Valid Teacher teacher, BindingResult result) {
        isDefault = false;
        if(result.hasErrors()) {
            return "editTeacher";
        }
        teacherDao.updateTeacher(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("deleteTeacher")
    public String deleteTeacher(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        teacherDao.deleteTeacherById(id);
        return "redirect:/teachers";
    }
}

