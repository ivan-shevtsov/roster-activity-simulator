package roster.roster.domain.generator;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import roster.domain.model.Grade;
import roster.domain.model.Section;
import roster.domain.model.SectionId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SectionFactory {

    public static final List<String> COURSES = new ArrayList<>();

    static {
        COURSES.add("Math");
        COURSES.add("Biology");
        COURSES.add("Reading");
        COURSES.add("Writing");
        COURSES.add("Chemistry");
        COURSES.add("Physics");
    }

    private Faker faker;

    public SectionFactory(Faker faker) {
        this.faker = faker;
    }

    public Section newSection(Grade grade) {
        Section section = new Section(SectionId.randomSectionId());
        section.setGrade(grade);

        String course = COURSES.get(RandomUtils.nextInt(0, COURSES.size()));
        section.setName(course + "-" + grade.getGradeValue() + RandomStringUtils.randomAlphabetic(1));

        return section;
    }

}
