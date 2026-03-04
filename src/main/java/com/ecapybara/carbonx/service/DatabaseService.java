package com.ecapybara.carbonx.service;

import com.ecapybara.carbonx.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DatabaseService {

    @Autowired
    private WebClient webClient;
    @Autowired
    private WebClientConfig webClientConfig;
    public String getAllDatabases() {
        return webClient.get()
                .uri(webClientConfig.buildUri("/database", "_system"))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String createDatabase(String name) {
        String jsonDocument = String.format("""
        {
            "name": "%s"
        }
        """, name);

        return webClient
            .post()
            .uri(webClientConfig.buildUri("/database", "_system"))
            .bodyValue(jsonDocument)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String deleteDatabase(String name){
 
        return webClient
            .delete()
            // .uri("http://localhost:8529/_db/_system/_api/database/{name}",name)
            .uri(webClientConfig.buildUri("/database/{name}", "_system"),name)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}
