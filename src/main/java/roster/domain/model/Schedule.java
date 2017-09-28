package roster.domain.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public @Data class Schedule {

    Map<Integer, List<Section>> schedule = new HashMap<>(5);

    public void add(int lessonIndex, List<Section> sectionsForHour) {
        schedule.put(lessonIndex, sectionsForHour);
    }

}
