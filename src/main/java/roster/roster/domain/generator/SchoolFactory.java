package roster.roster.domain.generator;

import com.github.javafaker.Business;
import com.github.javafaker.Company;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import roster.domain.model.School;
import roster.domain.model.SchoolId;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SchoolFactory {

    List<String> zones = new ArrayList<>(ZoneId.getAvailableZoneIds());
    private Faker faker;

    public SchoolFactory(Faker faker) {
        this.faker = faker;
    }

    public School newSchool() {
        SchoolId schoolId = SchoolId.randomSchoolId();
        School school = new School(schoolId);

        String greatMan = faker.name().fullName();
        school.setSchoolName("School Of " + greatMan);

        String zone = zones.get(RandomUtils.nextInt(0, zones.size()));
        school.setTimeZone(TimeZone.getTimeZone(ZoneId.of(zone)));

        return school;
    }

}
