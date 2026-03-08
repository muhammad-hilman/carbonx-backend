package com.ecapybara.carbonx.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecapybara.carbonx.model.issb.Input;
import com.ecapybara.carbonx.model.issb.Product;
import com.ecapybara.carbonx.repository.ProductRepository;
import com.ecapybara.carbonx.service.DocumentService;
import com.ecapybara.carbonx.service.GraphService;
import com.ecapybara.carbonx.service.ImportExportService;
import com.ecapybara.carbonx.service.arango.ArangoDatabaseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
  
   @Autowired
   private ArangoDatabaseService databaseService;
   @Autowired
   private ImportExportService importService;
   @Autowired
   private DocumentService documentService;
   @Autowired
   private GraphService graphService;
   @Autowired
   private ProductRepository productRepository;
   @Autowired
   private ObjectMapper objectMapper;

   final Sort sort = Sort.by(Direction.DESC, "id");


   @GetMapping
   public Object searchAllProducts(@RequestParam(required = false, defaultValue = "global") String targetDatabase) {

      ObjectMapper mapper = new ObjectMapper();
      List<Product> result = new ArrayList<>();
      Map<String,Object> response = databaseService.listDatabases().block();
      List<String> databases = mapper.convertValue(response.get("result"), new TypeReference<List<String>>() {});
      databases.remove("_system");
      
      if (targetDatabase.equals("global")) {
         for (String database : databases) {
            response = documentService.getAllDocuments(database, "products").block();
            result = ListUtils.union(result, mapper.convertValue(response.get("result"), new TypeReference<List<Product>>() {}));
         }
         return result;

      } else if (databases.contains(targetDatabase)) { // returns whatever is in the specified database name
         response = documentService.getAllDocuments(targetDatabase, "products").block();
         return mapper.convertValue(response.get("result"), new TypeReference<List<Product>>() {});

      } else { // throws error because database is confirmed to be unidentified

         return documentService.getAllDocuments(targetDatabase, "products").block();
      }
   }
/*
   @PutMapping
   public List<Product> searchProducts(@RequestParam(required = true) String database) {

      if (name != null && type!=null && productOrigin!=null) {
         return productRepository.findByName(sort, name);
      }

      String query = "FOR v in products";
      
      if (name != null && !name.isEmpty()) {
         return productRepository.findByName(sort, name);
      }
      else if (type != null && !type.isEmpty()) {
         return productRepository.findByType(sort, type);
      }
      else {
         return IterableUtils.toList(productRepository.findAll());
      }
   }
*/
/* 
   @GetMapping
   public Iterable<Product> getProducts(Product criteria) {
      Example<Product> example = Example.of(criteria);
      Iterable<Product> iterable = productRepository.findAll(example);
      List<Product> result = new ArrayList<>();
      iterable.forEach(result::add);
      return result;
   }
*/

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(value = HttpStatus.CREATED)
   public List<Product> createProducts(@RequestBody List<Product> productsList) {
      documentService.createDocument("testCompany", null, productsList, null, null, null, null, null);
      
      for (Product product : productsList) {
         System.out.println("----- New product created:");
         System.out.println(product.toString());

         productRepository.save(product);
         product = productRepository.findByNameAndType(sort, product.getName(), product.getType()).get(0);
         System.out.println("Created product saved into product database:");
         System.out.println(product.toString());
      }

      return productsList;
   }

   @PutMapping
   public List<Product> editProducts(@RequestBody List<Product> revisedProducts) {
      for (Product productRevision : revisedProducts) {
      Product product = editProduct(productRevision.getId(), productRevision);
      productRevision = product; //replace the list element with the new entity from database
      }
      return revisedProducts;
   }

//   @GetMapping("/{key}")
//   public Mono<Product> getProduct(@PathVariable String key) {
//     Map<String,Object> rawDocument = documentService.getDocument("products", key, null, null)
//                                                     .block();
//     Product product = objectMapper.convertValue(rawDocument, Product.class);
//     return Mono.just(product);
//   }

   @GetMapping("/{id}")
   public String getProduct(@PathVariable String id, @RequestParam String fieldName) {
      String value = productRepository.findFieldByProductId(id, fieldName);
      return value != null ? value : "Field or Product not found";
   }


   @PutMapping("/{id}")
   public Product editProduct(@PathVariable String id, @RequestBody Product revisedProduct) {
      Product product = productRepository.findById(id).orElse(null);
      
      if (product != null) {
      product.setName(revisedProduct.getName());
      product.setType(revisedProduct.getType());
      product.setProductOrigin(revisedProduct.getProductOrigin());
      product.setDPP(revisedProduct.getDPP());
      product.setUserId(revisedProduct.getUserId());
      product.setUploadedFile(revisedProduct.getUploadedFile());
      productRepository.save(product);
      }
      
      return productRepository.findById(id).orElse(null);
   }

  // Proper document deletion require the use of ArangoDB's Graph API since AQL does not cleanly delete hanging edges. Trust me, I've tried
   @DeleteMapping("/{id}")
   public Mono<Boolean> removeProduct(@PathVariable String id) {
      return graphService.deleteDocuments("products", id);
   }

  /*
  @GetMapping("/{id}/calculate")
  public Mono<?> calculateProduct(@PathVariable String id) {
    Product product = productRepository.findById(id).orElse(null);
    return LCAService.calculate(product);
  }
  */

  // Experimental endpoint to call for backend import function for products
   @PostMapping("/import")
   public Mono<?> testImport() {
      String dir = System.getProperty("user.dir");
      Path filepath = Paths.get(dir,"temp");
      
      List<String> files = List.of("templates.csv");
      
      return Flux.fromIterable(files)
         .flatMap(filename -> importService.importCSV( filepath.resolve(filename), "testCompany", "products"))
         .then(Mono.just("Successfully imported JSON files!"))
         .onErrorReturn("Import failed - check logs");
   }
}