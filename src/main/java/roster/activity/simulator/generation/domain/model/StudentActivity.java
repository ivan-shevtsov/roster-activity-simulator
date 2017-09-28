package roster.activity.simulator.generation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import roster.domain.model.Application;
import roster.domain.model.School;
import roster.domain.model.Section;
import roster.domain.model.Student;

import java.time.Instant;

public @Data @AllArgsConstructor
class StudentActivity {
    private School school;
    private Section section;
    private Student student;
    private Application application;
}
