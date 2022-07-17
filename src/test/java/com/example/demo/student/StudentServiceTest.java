package com.example.demo.student;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


//@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private AutoCloseable autoCloseable;
    private StudentService underTest;
    
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }

    
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
    
    
    @Test
    void getStudents() {
        //when
        underTest.getStudents();

        //then
        verify(studentRepository).findAll();
    }
    
    @Test
    void addNewStudent() {
        //given
        Student student = new Student(
            1l,
            "Jamil",
            "jamil@gmail.com",
            LocalDate.of(2005, Month.MARCH, 5)
            
        );

        //when
        underTest.addNewStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
            ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void throwErrorWhenAddNewStudentEmailTaken() {
        //given
        Student student = new Student(
            1l,
            "Jamil",
            "jamil@gmail.com",
            LocalDate.of(2005, Month.MARCH, 5)
            
        );

        boolean studentFound =studentRepository.selectExistingEmail(student.getEmail());

        given(studentFound).willReturn(true);

        //when

        //then
        assertThatThrownBy(() -> underTest.addNewStudent(student))
            .isInstanceOf( IllegalStateException.class)
            .hasMessageContaining("email Taken!");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent() {
        //given
        Student student = new Student(
            1l,
            "Jamil",
            "jamil@gmail.com",
            LocalDate.of(2005, Month.MARCH, 5)
            
        );

        underTest.addNewStudent(student);

        boolean exists = studentRepository.existsById(student.getId());

        assertThat(exists).isTrue();

        //when
        underTest.deleteStudent(student.getId());

        //then
        boolean notExists = studentRepository.existsById(student.getId());

        assertThat(notExists).isFalse();

    }

    @Test
    @Disabled
    void updateStudent() {

    }

}
