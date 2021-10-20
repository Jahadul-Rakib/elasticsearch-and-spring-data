package com.database_example.service;

import com.database_example.exception.APIException;
import com.database_example.model.Student;
import com.database_example.model.StudentDTO;

import java.util.List;

public interface StudentService {
    Student createStudent(StudentDTO student);
    List<Student> getStudents(String sName, int age);
    List<Student> searchStudents(String sName);
    List<Student> multiQuerySearch(String text);

    List<Student> testQuery(String s) throws APIException;
}
