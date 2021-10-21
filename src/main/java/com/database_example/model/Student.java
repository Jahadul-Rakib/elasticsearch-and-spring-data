package com.database_example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "student")
public class Student {
    @Id
    @Field(type = FieldType.Auto, value = "studentID")
    private String studentID;
    @Field(type = FieldType.Text, value = "studentName")
    private String studentName;
    @Field(type = FieldType.Keyword, value = "departmentName")
    private String departmentName;
    @Field(type = FieldType.Integer, value = "age")
    private int age;
    @Field(type = FieldType.Date, value = "dateOfBirth")
    private Date dateOfBirth;
}
