package roster.domain.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public @Data class Student extends RosterEntity<StudentId> {

    public static final int MIN_SECONDS_BETWEEN_ACTIONS = 30;
    private Grade grade;
    private LocalDateTime lastTimeOfPerformedAction;

    public Student(StudentId entityId) {
        super(entityId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return grade == student.grade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), grade);
    }

    public boolean performActionAt(LocalDateTime simulatedInstant) {
        if (lastTimeOfPerformedAction == null ||
                Duration.between(lastTimeOfPerformedAction, simulatedInstant).getSeconds() > MIN_SECONDS_BETWEEN_ACTIONS) {
            this.lastTimeOfPerformedAction = simulatedInstant;
            return true;
        }
        return false;
    }
}
