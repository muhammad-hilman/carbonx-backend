package com.ecapybara.carbonx.runner;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import com.arangodb.springframework.core.ArangoOperations;
import com.ecapybara.carbonx.service.ExperimentalService;

@Slf4j
@ComponentScan("com.ecapybara.carbonx")
public class ImportExperimentSetup implements CommandLineRunner {
  @Autowired
  private ArangoOperations operations;
  @Autowired
  private ExperimentalService experimentalService;
  
  @Override
  public void run(final String... args) throws Exception {
    System.out.println("-------------- # SETUP BEGIN # --------------");
    // first drop the database so that we can run this multiple times with the same dataset
    operations.dropDatabase();

    // Import complexProducts.csv
    String outcome = experimentalService.importComplexCSV("products", "complexProducts.csv");

    // Display outcome
    log.info("Import experiment outcome -> {}", outcome);

    System.out.println("------------- # SETUP COMPLETED # -------------");
  }
}