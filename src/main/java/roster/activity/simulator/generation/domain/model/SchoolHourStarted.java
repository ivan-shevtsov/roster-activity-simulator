package roster.activity.simulator.generation.domain.model;

import lombok.Data;

import java.util.EventObject;

public @Data class SchoolHourStarted extends EventObject {

    public SchoolHourStarted(EmulationHour schoolHour) {
        super(schoolHour);
    }

    public EmulationHour getSchoolHour() {
        return (EmulationHour) getSource();
    }
}
