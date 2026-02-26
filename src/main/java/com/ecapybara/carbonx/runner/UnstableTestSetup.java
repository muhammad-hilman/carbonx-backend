package com.ecapybara.carbonx.runner;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import com.arangodb.ArangoDatabase;
import com.arangodb.springframework.core.ArangoOperations;
import com.ecapybara.carbonx.model.basic.EdgeDefinition;
import com.ecapybara.carbonx.model.basic.Graph;
import com.ecapybara.carbonx.repository.*;
import com.ecapybara.carbonx.service.GraphService;
import com.ecapybara.carbonx.service.ImportExportService;
import com.ecapybara.carbonx.service.arango.ArangoDatabaseService;

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
  private GraphService graphService;
  @Autowired
  private ImportExportService importExportService;
  
  @Override
  public void run(final String... args) throws Exception {
    System.out.println("------------- # SETUP BEGIN # -------------");
    // Check for appropriate databases and create if not present
    List<String> databases = (List<String>) databaseService.listDatabases().block().get("result");
    if (!databases.contains("default")) { databaseService.createDatabase("default", null, null, null, null); }
    if (!databases.contains("testCompany")) { databaseService.createDatabase("testCompany", null, null, null, null); }
    
    // For each database, nuke the existing dataset
    
    // Initialise new data

    // first drop the database so that we can run this multiple times with the same dataset
    operations.dropDatabase();

    // Create and save products
    String dir = System.getProperty("user.dir");
    String filename = "testProducts.csv";
    Path filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "products");
    log.info("-> {} PRODUCT entries created", productRepository.count());

    // Create and save processes
    filename = "testProcesses.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "processes");
    log.info("-> {} PROCESS entries created", processRepository.count());

    // Create and save input relationships between entities
    filename = "testInputs.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "inputs");
    log.info("-> {} INPUTS entries created", inputRepository.count());

    // Create and save outputs relationships between entities
    filename = "testOutputs.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "outputs");
    log.info("-> {} OUTPUTS entries created", outputRepository.count());

    /*
    // Create and save impact categories
    filename = "testImpactCategories.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "impactCategories");
    log.info("-> {} IMPACT CATEGORY entries created", impactCategoryRepository.count());

    // Create and save GWPs
    filename = "testGWPs.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "gwp");
    log.info("-> {} GWP entries created", GWPRepository.count());

    // Create and save metrics
    filename = "testMetrics.csv";
    filepath = Paths.get(dir,"src", "main", "resources", "data", "test").resolve(filename);
    importExportService.importCSV(filepath, "metrics");
    log.info("-> {} METRIC entries created", metricRepository.count());
     */

    // Create graph
    EdgeDefinition inputs = new EdgeDefinition("inputs", List.of("products"), List.of("processes"));
    EdgeDefinition outputs = new EdgeDefinition("outputs", List.of("processes"), List.of("products"));
    Graph defaultGraph = new Graph("default", List.of(inputs, outputs));
    graphService.createGraph(defaultGraph)
        .doOnSuccess(graph -> log.info("Graph created: {}", graph))
        .doOnError(error -> log.error("Failed to create graph", error))
        .block();  // Wait for completion (OK in CommandLineRunner); // IMPORTANT NOTE: I don't know why it works, but the .subscribe() is crucial to make the graph

    // Export files
    // importExportService.exportCSV("products", "exportProducts.csv").doOnError(error -> log.error("Failed to export PRODUCTS -> ", error));
    // importExportService.exportCSV("processes", "exportProcesses.csv").doOnError(error -> log.error("Failed to export PROCESSES -> ", error));
    // log.info("-> Successfully exported PROCESSES into complexProcesses.csv");
    // importExportService.exportCSV("inputs", "exportInputs.csv").doOnError(error -> log.error("Failed to export INPUTS -> ", error));
    // importExportService.exportCSV("outputs", "exportOutputs.csv").doOnError(error -> log.error("Failed to export OUTPUTS -> ", error));
    
    System.out.println("------------- # SETUP COMPLETED # -------------");
  }
}