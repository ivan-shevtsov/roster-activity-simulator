package roster.activity.simulator.generation;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import roster.domain.model.Grade;
import roster.domain.model.Schedule;
import roster.domain.model.School;
import roster.domain.model.SchoolCreated;
import roster.domain.model.Section;
import roster.domain.model.Student;
import roster.roster.domain.generator.SectionFactory;
import roster.roster.domain.generator.StudentFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SectionStrategy {

    private final Logger logger = LoggerFactory.getLogger(SectionStrategy.class);

//    @Value("sections.per.grade")
    private Integer sectionsPerGrade = 8;

//    @Value("sections.students.count")
    private Integer studentsInTheSection = 30;

    private Integer sectionsPerHourForEachGrade = 3;


    private final SectionFactory sectionFactory;
    private final ApplicationEventPublisher eventPublisher;

    public SectionStrategy(SectionFactory sectionFactory, ApplicationEventPublisher eventPublisher) {
        this.sectionFactory = sectionFactory;
        this.eventPublisher = eventPublisher;
    }


    @EventListener(SchoolCreated.class)
    @Order(2)
    public void createSectionsInNewSchool(SchoolCreated schoolCreated) {
        School school = schoolCreated.getSchool();
        logger.info("\t Generate sections [schoolName={}]", school.getSchoolName());

        Stream.of(Grade.class.getEnumConstants())
              .flatMap(grade -> createSections(school, grade))
              .forEach(this::enrollStudents);

    }

    private void enrollStudents(Section section) {
        logger.info("\t\t Enroll students [sectionName={}]", section.getName());
        School school = section.getSchool();
        List<Student> students = school.getStudentsByGrades().get(section.getGrade());

        int countOfStudentsToEnroll = Integer.min(students.size(), studentsInTheSection);
        Set<Student> studentsForSection = new HashSet<>();

        IntStream.range(0, countOfStudentsToEnroll)
                 .map(index -> RandomUtils.nextInt(0, students.size()))
                 .forEach(index -> {
                     IntStream.concat(IntStream.range(index, students.size()),
                                      IntStream.range(0, index))
                             .mapToObj(students::get)
                             .filter(student -> !studentsForSection.contains(student))
                             .findAny()
                             .ifPresent(studentsForSection::add);
                 });

        section.setStudents(studentsForSection);
    }

    @EventListener(SchoolCreated.class)
    @Order(3)
    public void createSchedule(SchoolCreated schoolCreated) {
        School school = schoolCreated.getSchool();
        Schedule schedule = new Schedule();
        IntStream.range(0, 5)
                 .forEach(lessonIndex -> {
                     List<Section> sectionsForHour = new ArrayList<>();
                     Map<Grade, List<Section>> sectionsByGrades = school.getSectionsByGrades();
                     sectionsByGrades.keySet().stream()
                                              .flatMap(grade -> school.getRandomSections(grade, sectionsPerHourForEachGrade))
                                              .forEach(sectionsForHour::add);
                     schedule.add(lessonIndex, sectionsForHour);
                 });
        school.setSchedule(schedule);
    }

    private Stream<Section> createSections(School school, Grade grade) {
        return IntStream.range(0, sectionsPerGrade)
                 .mapToObj(index -> sectionFactory.newSection(grade))
                 .peek(school::addSection);
    }


}
