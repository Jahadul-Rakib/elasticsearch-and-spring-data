package com.database_example.controller;


import com.database_example.exception.APIException;
import com.database_example.model.Student;
import com.database_example.model.StudentDTO;
import com.database_example.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/es/student")
public class ESController {

    private final StudentService studentService;

    public ESController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }

    @GetMapping
    public ResponseEntity<?> getStudent(@RequestParam String name,
                                        @RequestParam int age) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents(name, age));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchStudent(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.searchStudents(name));
    }
    @GetMapping("/multi-search")
    public ResponseEntity<?> searchByAnyField(@RequestParam String text) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.multiQuerySearch(text));
    }

    @GetMapping("/query")
    public ResponseEntity<?> searchAnyField(@RequestParam String value) throws APIException {
        List<Student> studentList = studentService.testQuery(value);
        return ResponseEntity.status(HttpStatus.OK).body(studentList);
    }
}
