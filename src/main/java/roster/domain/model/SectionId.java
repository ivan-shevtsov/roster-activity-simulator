package roster.domain.model;

import java.util.UUID;

public class SectionId extends RosterEntityId {

    public SectionId(String idValue) {
        super(idValue);
    }

    public static SectionId randomSectionId() {
        return new SectionId(UUID.randomUUID().toString());
    }
}
