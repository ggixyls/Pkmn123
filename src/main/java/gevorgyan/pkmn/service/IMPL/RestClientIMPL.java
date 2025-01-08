package gevorgyan.pkmn.service.IMPL;

import gevorgyan.pkmn.service.RestClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClientIMPL implements RestClient {

    private final RestTemplate restTemplate;

    public RestClientIMPL(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public String get(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}