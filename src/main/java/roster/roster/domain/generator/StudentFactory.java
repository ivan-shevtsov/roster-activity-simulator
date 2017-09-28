package roster.roster.domain.generator;

import com.github.javafaker.Faker;
import roster.domain.model.Grade;
import roster.domain.model.Student;
import roster.domain.model.StudentId;

public class StudentFactory {

    private Faker faker;

    public StudentFactory(Faker faker) {
        this.faker = faker;
    }

    public Student newStudent(Grade grade) {
        StudentId studentId = StudentId.randomStudentId();
        Student student = new Student(studentId);
        student.setGrade(grade);
        return student;
    }

}
