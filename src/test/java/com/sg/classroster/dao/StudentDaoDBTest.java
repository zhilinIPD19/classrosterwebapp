package com.sg.classroster.dao;

import com.sg.classroster.entities.Course;
import com.sg.classroster.entities.Student;
import com.sg.classroster.entities.Teacher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class StudentDaoDBTest {

    @Autowired
    TeacherDao teacherDao;
    @Autowired
    StudentDao studentDao;
    @Autowired
    CourseDao courseDao;

    public StudentDaoDBTest(){

    }
    @BeforeEach
    void setUp() {
        List<Teacher> teacherList = teacherDao.getAllTeachers();
        for(Teacher teacher:teacherList){
            teacherDao.deleteTeacherById(teacher.getId());
        }

        List<Student>  studentList= studentDao.getAllStudents();
        for(Student student:studentList){
            studentDao.deleteStudentById(student.getId());
        }

        List<Course> courseList = courseDao.getAllCourses();
        for(Course course:courseList){
            courseDao.deleteCourseById(course.getId());
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testAddAndGetStudent(){
        Student student = new Student();
        student.setFirstName("Zin");
        student.setLastName("Lin");
        student = studentDao.addStudent(student);

        Student fromDao = studentDao.getStudentById(student.getId());
        assertEquals(student, fromDao);
    }

    @Test
    public void testGetAllStudents(){
        Student student = new Student();
        student.setFirstName("Zin");
        student.setLastName("Lin");
        student = studentDao.addStudent(student);

        Student student2 = new Student();
        student2.setFirstName("Zin2");
        student2.setLastName("Lin2");
        student2 = studentDao.addStudent(student2);

        List<Student> students = studentDao.getAllStudents();

        assertEquals(2, students.size());
        assertTrue(students.contains(student));
        assertTrue(students.contains(student2));
    }

    @Test
    public void TestUpdateStudent(){
        Student student = new Student();
        student.setFirstName("Zin");
        student.setLastName("Lin");
        student = studentDao.addStudent(student);

        Student fromDao = studentDao.getStudentById(student.getId());
        assertEquals(student, fromDao);

        student.setFirstName("Zin2");
        studentDao.updateStudent(student);

        assertNotEquals(student, fromDao);

        fromDao = studentDao.getStudentById(student.getId());

        assertEquals(student,fromDao);
    }

    @Test
    public void testDeleteStudentById(){
        Teacher teacher = new Teacher();
        teacher.setFirstName("t1");
        teacher.setLastName("t2");
        teacher.setSpecialty("sp");
        teacher = teacherDao.addTeacher(teacher);

        Student student = new Student();
        student.setFirstName("Zin");
        student.setLastName("Lin");
        student = studentDao.addStudent(student);

        List<Student> students = new ArrayList<>();
        students.add(student);

        Course course = new Course();
        course.setName("c");
        course.setStudents(students);
        course.setTeacher(teacher);
        course = courseDao.addCourse(course);

        Student fromDao = studentDao.getStudentById(student.getId());
        assertEquals(student, fromDao);

        studentDao.deleteStudentById(student.getId());
        fromDao = studentDao.getStudentById(student.getId());
        assertNull(fromDao);
    }
}