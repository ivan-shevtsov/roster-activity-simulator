package roster.activity.simulator.generation.domain.model;

import lombok.Data;

import java.time.LocalDate;

public @Data class SchoolDay {

    private LocalDate date;

    public SchoolDay(LocalDate date) {
        this.date = date;
    }

}
