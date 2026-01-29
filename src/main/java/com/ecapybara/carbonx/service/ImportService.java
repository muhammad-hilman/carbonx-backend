package com.ecapybara.carbonx.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ImportService {

  @Autowired
  private WebClient webClient;

  //Test Function to import a single JSON file within system folder
  public Mono<?> importJSON(String targetCollection, String filename) {
    try {
        System.out.println(filename);
        Resource resource = new ClassPathResource(filename);
        Path filePath = resource.getFile().toPath();
        String jsonContent = Files.readString(filePath);
        String type = "documents";
        return webClient.post()
                .uri("/import?collection={targetCollection}?type={type}", targetCollection, type)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonContent)
                .retrieve()
                .bodyToMono(String.class);
    } catch (IOException e) {
        return Mono.error(new RuntimeException(String.format("Failed to load JSON file: %s", filename), e));
    }
  }

  //Test Function to import a single CSV file within system folder
  public Mono<Void> importCSV(String targetCollection, String filename) {
    return null;
  }
}
