package roster.activity.simulator.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import roster.activity.simulator.generation.domain.model.SchoolDay;
import roster.activity.simulator.generation.domain.model.SchoolDayStarted;
import roster.activity.simulator.generation.domain.model.EmulationHour;
import roster.activity.simulator.generation.domain.model.SchoolHourStarted;
import roster.activity.simulator.generation.domain.model.StudentActivity;
import roster.activity.simulator.generation.domain.model.StudentActivityPerformed;
import roster.domain.model.Application;
import roster.domain.model.School;
import roster.domain.model.SchoolCreated;
import roster.domain.model.Section;
import roster.domain.model.Student;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

public class StudentActivityStrategy {

    private Logger logger = LoggerFactory.getLogger(StudentActivityStrategy.class);
    private int daysToSimulate = 2;
    private long realSecondsPerSimulatedHour = 1;

    private ThreadPoolExecutor executorService;
    private ApplicationEventPublisher eventPublisher;

    private List<School> schools = new ArrayList<>();

    public StudentActivityStrategy(ThreadPoolExecutor executorService, ApplicationEventPublisher eventPublisher) {
        this.executorService = executorService;
        this.eventPublisher = eventPublisher;
    }

    @Order(100)
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationIsReady() {

        LocalDate localDate = LocalDate.of(2017, 9, 1);

        new Thread(() -> {

            IntStream.range(0, daysToSimulate)
                    .mapToObj(localDate::plusDays)
                    .map(SchoolDay::new)
                    .map(SchoolDayStarted::new)
                    .forEach(eventPublisher::publishEvent);

        }).start();

    }

    @EventListener(SchoolDayStarted.class)
    public void onNewSchoolDay(SchoolDayStarted schoolDayStarted) {
        SchoolDay schoolDay = schoolDayStarted.getSchoolDay();
        logger.info("Emulating new Day [schoolDay={}]", schoolDay.getDate());

        IntStream.range(0, 24)
                 .mapToObj(hour -> new EmulationHour(schoolDay, hour))
                 .map(SchoolHourStarted::new)
                 .forEach(eventPublisher::publishEvent);
    }

    @EventListener(SchoolHourStarted.class)
    public void scheduleSingleHourActivity(SchoolHourStarted schoolHourStarted) {
        logger.info("New hour [date={},h24={}]", schoolHourStarted.getSchoolHour().getSchoolDay().getDate(), schoolHourStarted.getSchoolHour().getHour24());
        Instant simulationStartedAt = Instant.now();
        Instant simulationEndedAt = simulationStartedAt.plusSeconds(realSecondsPerSimulatedHour);

        schools.stream()
               .filter(school -> school.isWorkable(schoolHourStarted.getSchoolHour()))
               .flatMap(school -> school.getSectionsByHour(schoolHourStarted.getSchoolHour()).stream())
               .peek(section -> section.selectApplication())
               .map(section -> generateRandomActivityUntilTheEndOfTheHour(section, schoolHourStarted, simulationStartedAt, simulationEndedAt))
               .forEach(executorService::submit);

        Object lock = new Object();


        try {
            while (!executorService.getQueue().isEmpty()) {
                logger.info("Executed tasks [count={}]",executorService.getTaskCount());
                synchronized (lock) {
                    lock.wait(1_000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Runnable generateRandomActivityUntilTheEndOfTheHour(Section section, SchoolHourStarted schoolHourStarted, Instant simulationStartedAt, Instant simulationEndedAt) {
        return () -> {
            Instant simulationInstant = Instant.now();
            if (simulationInstant.isBefore(simulationEndedAt)) {

                long periodInMiliseconds = simulationEndedAt.toEpochMilli() - simulationStartedAt.toEpochMilli();
                long fromStartOfPeriod = simulationInstant.toEpochMilli() - simulationStartedAt.toEpochMilli();

                long emulatedMillis = (long) (1.0 * fromStartOfPeriod / periodInMiliseconds * (60 * 60 * 1000));

                EmulationHour emulationHour = schoolHourStarted.getSchoolHour();

                try {
                    LocalDateTime simulatedInstant = LocalDateTime.of(emulationHour.getSchoolDay().getDate(),
                            emulationHour.asLocalTime().plus(emulatedMillis, ChronoUnit.MILLIS));

                    Student student = section.getRandomStudent();
                    if (student.performActionAt(simulatedInstant)) {
                        StudentActivity studentActivity = new StudentActivity(section.getSchool(), section, student, section.getApplication());
                        eventPublisher.publishEvent(new StudentActivityPerformed(studentActivity, simulatedInstant.atZone(ZoneId.of("UTC")).toInstant()));
                    }

                    executorService.submit(generateRandomActivityUntilTheEndOfTheHour(section, schoolHourStarted, simulationStartedAt, simulationEndedAt));
                } catch (Exception e) {
                    logger.error("Failed to generate random activity", e);
                }
            }
        };
    }

    @Order(100)
    @EventListener(SchoolCreated.class)
    public void onNewSchoolCreated(SchoolCreated schoolCreated) {
        schools.add(schoolCreated.getSchool());
    }

}
