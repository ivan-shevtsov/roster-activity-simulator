package roster.domain.model;

import java.util.UUID;

/**
 * School's unique identifier
 */
public class SchoolId extends RosterEntityId {

    protected SchoolId(String idValue) {
        super(idValue);
    }

    public static SchoolId randomSchoolId() {
        return new SchoolId(UUID.randomUUID().toString());
    }
}
