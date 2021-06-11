package com.sg.classroster.dao;

import com.sg.classroster.entities.Teacher;

import java.util.List;

public interface TeacherDao {

    Teacher getTeacherById(int id);
    List<Teacher> getAllTeachers();
    Teacher addTeacher(Teacher teacher);
    void UpdateTeacher(Teacher teacher);
    void deleteTeacherById(int id);
}
