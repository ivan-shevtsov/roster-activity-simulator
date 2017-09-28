package roster.domain.model;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public @Data
class Section extends RosterEntity<SectionId> {

    private School school;
    private Grade grade;
    private List<Student> students = new ArrayList<>();
    private String name;

    private Application application;

    public Section(SectionId entityId) {
        super(entityId);
    }

    public void setStudents(Collection<Student> students) {
        this.students.addAll(students);
    }

    public Student getRandomStudent() {
        return students.get(RandomUtils.nextInt(0, students.size()));
    }

    public void selectApplication() {
        application = Application.randomApplication();
    }
}
