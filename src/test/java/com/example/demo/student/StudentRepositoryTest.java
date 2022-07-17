package com.example.demo.student;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StudentRepositoryTest {
    
    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findStudentByEmail() {
        //given
        Student student = new Student(
            1l,
            "Jamil",
            "jamil@gmail.com",
            LocalDate.of(2005, Month.MARCH, 5)
            
        );

        underTest.save(student);

        //when
        boolean studentFound = underTest.findStudentByEmail("jamil@gmail.com").isPresent();
        
        //then
        assertThat(studentFound).isTrue();

    }
}
