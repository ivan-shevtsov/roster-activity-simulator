package roster.activity.simulator.bridge.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.function.Consumer;

public class HttpBridge implements Consumer<PluginMessage> {

    public static long maxTime = 0;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void accept(PluginMessage pluginMessage) {

        try {
            long before = System.currentTimeMillis();
            restTemplate.postForLocation("http://192.168.0.90:8080/request", pluginMessage);
//            System.out.println(new ObjectMapper().writeValueAsString(pluginMessage));
            long after = System.currentTimeMillis();

            long delta = after - before;
            if (delta > maxTime) {
                maxTime = delta;
                System.out.println(delta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
