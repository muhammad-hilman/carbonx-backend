package com.ecapybara.carbonx.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecapybara.carbonx.model.basic.Product;
import com.ecapybara.carbonx.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ImportService {

  @Autowired
  private WebClient webClient;
  @Autowired
  private ProductRepository productRepository;

  //Test Function to import a single JSON file within system folder
  public Mono<?> importJSON(String targetCollection, String filename) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      // Convert file to JSON string (future implementation: JSON String received via HTTP)
      log.info("Requested filename: {}", filename);
      Resource resource = new ClassPathResource("samples/spaghetti/products/" + filename);
      Path filePath = resource.getFile().toPath();
      log.info("Requested filepath: {}", filePath);
      String jsonContent = Files.readString(filePath);
      log.info("JSON String loaded: {}", jsonContent.substring(0, 100) + "...");

      // Use object mapper to convert JSON String content to Product object
      Product newProduct = mapper.readValue(jsonContent, Product.class);
      log.info("{}", newProduct.toString());

      // Save new object into ProductRepository
      productRepository.save(newProduct);
      return Mono.just(String.format("Import successful for JSON file: %s", filename));

    } catch (IOException e) {
      log.error("Error processing JSON", e);
      return Mono.error(new RuntimeException(String.format("Failed to load JSON file: %s", filename), e));
    }
  }

  //Test Function to import a single CSV file within system folder
  public Mono<Void> importCSV(String targetCollection, String filename) {
    return null;
  }

  //Test Function to import a single JSON file within system folder
  public Mono<?> oldImportJSON(String targetCollection, String filename) {
    try {
        System.out.println(filename);
        Resource resource = new ClassPathResource("samples/spaghetti/products/" + filename);
        Path filePath = resource.getFile().toPath();
        System.out.println(filePath);
        String jsonContent = Files.readString(filePath);
        System.out.println(jsonContent);
        ObjectMapper mapper = new ObjectMapper();
        String jsonlContent = mapper.writeValueAsString(mapper.readTree(jsonContent)); // converts the JSON content into JSONL

        String type = "auto";
        return webClient.post()
                .uri("/import?collection={targetCollection}&type={type}&waitForSync=true", targetCollection, type)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonlContent)
                .retrieve()
                .bodyToMono(String.class);
    } catch (IOException e) {
        return Mono.error(new RuntimeException(String.format("Failed to load JSON file: %s", filename), e));
    }
  }
}
