package com.sg.classroster.dao;

import com.sg.classroster.entities.Course;
import com.sg.classroster.entities.Student;
import com.sg.classroster.entities.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CourseDaoDB implements CourseDao{
    @Override
    public Course getCourseById(int id) {
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public Course addCourse(Course course) {
        return null;
    }

    @Override
    public void updateCourse(Course course) {

    }

    @Override
    public void deleteCourseById(int id) {

    }

    @Override
    public List<Course> getCoursesForTeacher(Teacher teacher) {
        return null;
    }

    @Override
    public List<Course> getCoursesForStudent(Student student) {
        return null;
    }
}
