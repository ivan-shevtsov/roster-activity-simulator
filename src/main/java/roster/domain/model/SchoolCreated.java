package roster.domain.model;

import java.util.EventObject;

public class SchoolCreated extends EventObject {


    /**
     * Constructs en event with new school
     *
     * @param school The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public SchoolCreated(School school) {
        super(school);
    }

    public School getSchool() {
        return School.class.cast(getSource());
    }
}
