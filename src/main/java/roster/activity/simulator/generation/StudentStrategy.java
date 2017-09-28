package roster.activity.simulator.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import roster.domain.model.Grade;
import roster.domain.model.School;
import roster.domain.model.SchoolCreated;
import roster.domain.model.Student;
import roster.roster.domain.generator.StudentFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StudentStrategy {

    Logger logger = LoggerFactory.getLogger(StudentStrategy.class);

    private StudentFactory studentFactory;
    private ApplicationEventPublisher eventPublisher;

    private Integer studentsPerGrade =  100;

    public StudentStrategy(StudentFactory studentFactory, ApplicationEventPublisher eventPublisher) {
        this.studentFactory = studentFactory;
        this.eventPublisher = eventPublisher;
    }


    @EventListener(SchoolCreated.class)
    @Order(1)
    public void createStudentsInNewSchool(SchoolCreated schoolCreated) {
        School school = schoolCreated.getSchool();
        logger.info("\tGenerate students [schoolName={}]", school.getSchoolName());

        Stream.of(Grade.class.getEnumConstants())
              .flatMap(this::createStudents)
              .forEach(school::enrollStudent);
    }

    private Stream<Student> createStudents(Grade grade) {
        return IntStream.range(0, studentsPerGrade)
                        .mapToObj(index -> studentFactory.newStudent(grade));
    }

}
