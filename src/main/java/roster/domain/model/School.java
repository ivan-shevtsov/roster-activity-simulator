package roster.domain.model;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import roster.activity.simulator.generation.domain.model.EmulationHour;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Domain model of the school
 */
public @Data class School extends RosterEntity<SchoolId> {

    public static final int FIRST_WORK_HOUR = 9;
    public static final int LAST_WORK_HOUR = 13;
    private TimeZone timeZone;
    private String schoolName;
    private Map<Grade, List<Student>> studentsByGrades = new EnumMap<Grade, List<Student>>(Grade.class);
    private Map<Grade, List<Section>> sectionsByGrades = new EnumMap<Grade, List<Section>>(Grade.class);
    private Schedule schedule;

    public School(SchoolId entityId) {
        super(entityId);
    }

    public void enrollStudent(Student student) {
        Grade grade = student.getGrade();
        studentsByGrades.computeIfAbsent(grade, gr -> new ArrayList<>())
                        .add(student);
    }

    public void addSection(Section section) {
        section.setSchool(this);
        sectionsByGrades.computeIfAbsent(section.getGrade(), grade -> new ArrayList<>())
                        .add(section);
    }

    public boolean isWorkable(EmulationHour emulationHour) {
        LocalDateTime emulationHourInSchoolZone = getEmulationHourInSchoolZone(emulationHour);
        int hourInSchoolZone = emulationHourInSchoolZone.getHour();
        return hourInSchoolZone >= FIRST_WORK_HOUR && hourInSchoolZone <= LAST_WORK_HOUR;
    }

    public LocalDateTime getEmulationHourInSchoolZone(EmulationHour emulationHour) {
        OffsetDateTime offsetDateTime = emulationHour.asUTCDateTime().toOffsetDateTime();
        ZonedDateTime zonedDateTime = offsetDateTime.atZoneSameInstant(timeZone.toZoneId());
        return zonedDateTime.toLocalDateTime();
    }

    public int getLessonIndexAt(EmulationHour emulationHour) {
        int hour = getEmulationHourInSchoolZone(emulationHour).getHour();
        return hour >= FIRST_WORK_HOUR && hour <= LAST_WORK_HOUR ? hour - FIRST_WORK_HOUR : -1;
    }

    public Stream<Section> getRandomSections(Grade grade, Integer sectionsPerHourForEachGrade) {
        List<Section> sections = sectionsByGrades.get(grade);
        return IntStream.range(0, sectionsPerHourForEachGrade)
                        .map(index -> RandomUtils.nextInt(0, sections.size()))
                        .mapToObj(sections::get);
    }

    public List<Section> getSectionsByHour(EmulationHour emulationHour) {
        int lessonIndexAt = getLessonIndexAt(emulationHour);
        return schedule.getSchedule().get(lessonIndexAt);
    }
}
