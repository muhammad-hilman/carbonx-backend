package com.ecapybara.carbonx.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecapybara.carbonx.model.issb.Product;
import com.ecapybara.carbonx.repository.ProductRepository;
import com.opencsv.bean.CsvToBeanBuilder;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
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
      log.info("JSON String loaded: {}", jsonContent.substring(0, 200) + "...");

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
  public Mono<?> importCSV(String targetCollection, String filename) {
    ObjectMapper mapper = new ObjectMapper();
    try {      
      // Find, load and read CSV file
      log.info("Requested filename: {}", filename);
      Resource resource = new ClassPathResource("samples/" + filename);
      Path filePath = resource.getFile().toPath();
      log.info("Requested filepath: {}", filePath);
      Reader reader = Files.newBufferedReader(filePath);

      // Convert CSV to product objects
      List<Product> products = new CsvToBeanBuilder<Product>(reader)
            .withType(Product.class)
            .withIgnoreLeadingWhiteSpace(true)
            .build()
            .parse();      

      // Convert CSV file to JSON string (future implementation: JSON String received via HTTP)
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      String jsonContent = mapper.writeValueAsString(products);
      log.info("JSON String loaded: {}", jsonContent);

      // Use object mapper to convert JSON String content to Product objects
      List<Product> listOfProducts = mapper.readValue(jsonContent, new TypeReference<List<Product>>(){});
      log.info("List of products: {}", listOfProducts.toString());
    
      // Save new object into ProductRepository
      for (Product product: listOfProducts) {
        productRepository.save(product);
      }

      return Mono.just(String.format("Import successful for CSV file: %s", filename));

    } catch (IOException e) {
      log.error("Error processing CSV", e);
      return Mono.error(new RuntimeException(String.format("Failed to load CSV file: %s", filename), e));
    }
  }

  //Test Function to import a single JSON file within system folder,  !! Object class is not recorded in database when using this method !!
  public Mono<?> httpImportJSON(String targetCollection, String filename) {
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
