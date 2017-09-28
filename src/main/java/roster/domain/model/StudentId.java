package roster.domain.model;

import java.util.UUID;

public class StudentId extends RosterEntityId {

    public StudentId(String idValue) {
        super(idValue);
    }

    public static StudentId randomStudentId() {
        return  new StudentId(UUID.randomUUID().toString());
    }
}
