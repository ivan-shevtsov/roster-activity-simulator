package roster.activity.simulator.generation.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public @Data class EmulationHour {

    private SchoolDay schoolDay;
    private int hour24;

    public EmulationHour(SchoolDay schoolDay, int hour24) {
        this.schoolDay = schoolDay;
        this.hour24 = hour24;
    }

    public LocalTime asLocalTime() {
        return LocalTime.of(hour24, 0);
    }

    public LocalDateTime asLocalDateTime() {
        return LocalDateTime.of(schoolDay.getDate(), LocalTime.of(hour24, 0));
    }

    public ZonedDateTime asUTCDateTime() {
        return LocalDateTime.of(schoolDay.getDate(), LocalTime.of(hour24, 0)).atZone(ZoneId.of("UTC"));
    }

}
