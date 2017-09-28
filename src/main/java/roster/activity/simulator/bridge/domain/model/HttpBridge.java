package roster.activity.simulator.bridge.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.function.Consumer;

public class HttpBridge implements Consumer<PluginMessage> {

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void accept(PluginMessage pluginMessage) {

//        try {
//            String json = new ObjectMapper().writeValueAsString(pluginMessage);
//            System.out.println(json);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
        try {
            restTemplate.postForLocation("http://192.168.0.103:8080/request", pluginMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
