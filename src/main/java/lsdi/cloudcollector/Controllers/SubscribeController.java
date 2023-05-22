package lsdi.cloudcollector.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lsdi.cloudcollector.Services.MqttService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
public class SubscribeController {
    MqttService mqttService = MqttService.getInstance();
    RestTemplate restTemplate = new RestTemplate();
    private String workerUrl = System.getenv("WORKER_URL");

    @PostMapping("/subscribe/{topic}")
    public void subscribe(@PathVariable String topic) {
        //TODO subscribe to fognode broker topic to receive events
        ObjectMapper mapper = new ObjectMapper();
        mqttService.subscribe(topic, (t, message) -> {
            //TODO post event to cloudworker
            new Thread(() -> {
                try {
                    System.out.println("Received message: " + message.getPayload());
                    Map<String, Object> event = mapper.readValue(message.getPayload(), Map.class);
                    restTemplate.postForObject(workerUrl + "/event", event, Map.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }
}
