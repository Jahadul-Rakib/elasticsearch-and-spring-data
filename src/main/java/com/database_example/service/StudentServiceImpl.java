package com.database_example.service;

import com.database_example.exception.APIException;
import com.database_example.model.Student;
import com.database_example.model.StudentDTO;
import com.database_example.repository.StudentRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ElasticsearchOperations elasticsearchTemplate;

    public StudentServiceImpl(StudentRepository studentRepository,
                              ElasticsearchOperations elasticsearchTemplate) {
        this.studentRepository = studentRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Student createStudent(StudentDTO student) {
        Student student1 = Student.builder()
                .studentName(student.getStudentName())
                .departmentName(student.getDepartmentName())
                .age(student.getAge())
                .dateOfBirth(Instant.parse(student.getDateOfBirth()))
                .build();
        return studentRepository.save(student1);
    }

    @Override
    public List<Student> getStudents(String sName, int age) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders
                .boolQuery()
                .must(matchQuery("studentName", sName))
                .must(matchQuery("age", age));
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();
        SearchHits<Student> searchHits = elasticsearchTemplate.search(nativeSearchQuery, Student.class);
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<Student> searchStudents(String sName) {
        String name = ".*" + sName + ".*";
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.regexpQuery("studentName", name))
                .build();
        SearchHits<Student> searchHits = elasticsearchTemplate.search(searchQuery, Student.class);
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<Student> multiQuerySearch(String text) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(text)
                        .minimumShouldMatch("25%")
                        .field("studentName")
                        .field("departmentName")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                .build();
        SearchHits<Student> searchHits = elasticsearchTemplate.search(searchQuery, Student.class);
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<Student> testQuery(String value) throws APIException{
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.queryStringQuery(value)
                        .lenient(true)
                        .field("studentName")
                        .field("departmentName"))
                .should(QueryBuilders.queryStringQuery("*" + value + "*")
                        .lenient(true)
                        .field("studentName")
                        .field("departmentName"));

        //FieldSortBuilder sort = SortBuilders.fieldSort("studentName").order(SortOrder.DESC);
        PageRequest page = PageRequest.of(0, 3);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                //.withSort(sort)
                .withPageable(page)
                .build();
        SearchHits<Student> studentSearchHits = elasticsearchTemplate.search(nativeSearchQuery, Student.class);
        return studentSearchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
