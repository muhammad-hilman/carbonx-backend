package com.ecapybara.carbonx.runner;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDatabase;
import com.arangodb.springframework.core.ArangoOperations;
import com.ecapybara.carbonx.model.basic.EdgeDefinition;
import com.ecapybara.carbonx.model.basic.Graph;
import com.ecapybara.carbonx.repository.*;
import com.ecapybara.carbonx.service.ImportExportService;
import com.ecapybara.carbonx.service.arango.ArangoCollectionService;
import com.ecapybara.carbonx.service.arango.ArangoDatabaseService;
import com.ecapybara.carbonx.service.arango.ArangoDocumentService;
import com.ecapybara.carbonx.service.arango.ArangoGraphService;

@Slf4j
@ComponentScan("com.ecapybara.carbonx")
public class UnstableTestSetup implements CommandLineRunner {
  @Autowired
  private ArangoOperations operations;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ProcessRepository processRepository;
  @Autowired
  private InputRepository inputRepository;
  @Autowired
  private OutputRepository outputRepository;
  // @Autowired
  // private ImpactCategoryRepository impactCategoryRepository;
  // @Autowired
  // private GWPRepository GWPRepository;
  // @Autowired
  // private MetricRepository metricRepository;

  @Autowired
  private ArangoDatabaseService databaseService;
  @Autowired
  private ArangoCollectionService collectionService;
  @Autowired
  private ArangoGraphService graphService;
  @Autowired
  private ImportExportService importExportService;
  
  @Override
  public void run(final String... args) throws Exception {
    System.out.println("------------- # SETUP BEGIN # -------------");
    // first drop the database so that we can run this multiple times with the same dataset
    List<String> databases = (List<String>) databaseService.listDatabases().block().get("result");
    if (databases.contains("default")) { databaseService.dropDatabase("default").block(); }
    databaseService.createDatabase("default", null, null, null, null).block(); 
    if (databases.contains("testCompany")) { databaseService.dropDatabase("testCompany").block(); }
    databaseService.createDatabase("testCompany", null, null, null, null).block();
    
    // Create collections
    collectionService.createCollection("default", "products", 2, true, null, null, null, null).block();
    collectionService.createCollection("default", "processes", 2, true, null, null, null, null).block();
    collectionService.createCollection("default", "inputs", 3, true, null, null, null, null).block();
    collectionService.createCollection("default", "outputs", 3, true, null, null, null, null).block();

    // Create edge definitions and graph
    Map<String,Object> inputs = Map.of( "collection", "inputs",
                                        "from", List.of("products"),
                                        "to", List.of("processes"));
    Map<String,Object> outputs = Map.of( "collection", "outputs",
                                        "from", List.of("processes"),
                                        "to", List.of("products"));
    graphService.createGraph("default", "default", List.of(inputs, outputs), null, null, null, null).block();

    // Create and save products
    String dir = System.getProperty("user.dir");
    String filename = "testProducts.csv";
    Path filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "default", "products").block();

    // Create and save processes
    filename = "testProcesses.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "default", "processes").block();

    // Create and save input relationships between entities
    filename = "testInputs.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "default", "inputs").block();

    // Create and save input relationships between entities
    filename = "testOutputs.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "default", "outputs").block();

    // Export files
    // importExportService.exportCSV("products", "exportProducts.csv").doOnError(error -> log.error("Failed to export PRODUCTS -> ", error));
    // importExportService.exportCSV("processes", "exportProcesses.csv").doOnError(error -> log.error("Failed to export PROCESSES -> ", error));
    // log.info("-> Successfully exported PROCESSES into complexProcesses.csv");
    // importExportService.exportCSV("inputs", "exportInputs.csv").doOnError(error -> log.error("Failed to export INPUTS -> ", error));
    // importExportService.exportCSV("outputs", "exportOutputs.csv").doOnError(error -> log.error("Failed to export OUTPUTS -> ", error));
    
    System.out.println("------------- # SETUP COMPLETED # -------------");
  }
}