package roster.activity.simulator.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import roster.domain.model.School;
import roster.domain.model.SchoolCreated;
import roster.roster.domain.generator.SchoolFactory;

import java.util.stream.IntStream;

public class SchoolStrategy {

    private final Logger logger = LoggerFactory.getLogger(SchoolStrategy.class);

    private SchoolFactory schoolFactory;
    private ApplicationEventPublisher eventPublisher;

    private int numberOfSchools = 3;

    public SchoolStrategy(SchoolFactory schoolFactory, ApplicationEventPublisher eventPublisher) {
        this.schoolFactory = schoolFactory;
        this.eventPublisher = eventPublisher;
    }

    @Order(0)
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationIsStarted() {
        generateSchools();
    }

    public void generateSchools() {
        IntStream.range(0, numberOfSchools)
                 .mapToObj(this::generateNewSchool)
                 .map(SchoolCreated::new)
                 .forEach(eventPublisher::publishEvent);
    }

    private School generateNewSchool(int index) {
        School school = schoolFactory.newSchool();
        logger.info("Generated school [schoolName={}]", school.getSchoolName());
        return school;
    }

}
