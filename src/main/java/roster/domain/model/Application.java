package roster.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public @AllArgsConstructor @Data
class Application {

    private static List<String> STRANGE_WORDS = Arrays.asList("images", "videos", "lessons", "play","go", "subj", "page", "smile");
    private static List<String> STRANGE_PARAMS = Arrays.asList("i", "v", "les", "doit","go", "sbj", "cnt", "rf");

    private static final List<Application>  APPLICATIONS = Arrays.asList(
            new Application("Moodle", "https://moddle.com"),
            new Application("KhanAcademy", "https://www.khanacademy.org"),
            new Application("Canvas", "https://canvas.com"),
            new Application("YouTube", "https://youtube.com"),
            new Application("Math365", "https://math365.com"),
            new Application("ReadingUp", "https://reading.yeap.com")
    );

    private String applicationId;
    private String baseUrl;

    public String randomAction() {
        int actions = RandomUtils.nextInt(1,3);
        int params = RandomUtils.nextInt(0,2);

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        for (int i = 0; i < actions; i++) {
            urlBuilder.append("/")
                      .append(STRANGE_WORDS.get(RandomUtils.nextInt(0, STRANGE_WORDS.size())))
                      .append("/")
                      .append(RandomStringUtils.randomAlphabetic(4));
        }

        if (params > 0) {
            urlBuilder.append("?");
            urlBuilder.append(IntStream.range(0, params)
                                       .mapToObj(index ->
                                               STRANGE_PARAMS.get(RandomUtils.nextInt(0, STRANGE_PARAMS.size())) + "=" + RandomStringUtils.randomAlphabetic(3))
                                       .collect(Collectors.joining("&")) );
        }

        return urlBuilder.toString();
    }

    public static Application randomApplication() {
        return APPLICATIONS.get(RandomUtils.nextInt(0, APPLICATIONS.size()));
    }

}
