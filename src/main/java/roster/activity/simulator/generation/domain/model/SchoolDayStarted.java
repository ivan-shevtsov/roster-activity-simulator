package roster.activity.simulator.generation.domain.model;

import java.util.EventObject;

public class SchoolDayStarted extends EventObject {


    /**
     * Constructs a prototypical Event.
     *
     * @param schoolDay The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public SchoolDayStarted(SchoolDay schoolDay) {
        super(schoolDay);
    }

    public SchoolDay getSchoolDay() {
        return SchoolDay.class.cast(getSource());
    }
}
