package roster.activity.simulator.generation.domain.model;

import lombok.Getter;

import java.time.Instant;
import java.util.EventObject;

public class StudentActivityPerformed extends EventObject {

    private @Getter  Instant simulatedInstant;

    public StudentActivityPerformed(StudentActivity studentActivity, Instant simulatedInstant) {
        super(studentActivity);
        this.simulatedInstant = simulatedInstant;
    }

    public StudentActivity getStudentActivity() {
        return StudentActivity.class.cast(getSource());
    }

}
