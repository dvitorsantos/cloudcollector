package lsdi.cloudcollector.Controllers;

import lsdi.cloudcollector.DataTransferObjects.DeployRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DeployController {
    RestTemplate restTemplate = new RestTemplate();
    private final String deployerUrl = System.getenv("DEPLOYER_URL");

    @PostMapping("/deploy")
    public void deploy(@RequestBody DeployRequest deployRequest) {
        System.out.println(deployRequest);
        restTemplate.postForObject(deployerUrl + "/deploy", deployRequest, String.class);
    }
}
