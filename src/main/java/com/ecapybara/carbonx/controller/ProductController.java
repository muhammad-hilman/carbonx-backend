package com.ecapybara.carbonx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecapybara.carbonx.model.issb.Product;
import com.ecapybara.carbonx.repository.ProductRepository;
import com.ecapybara.carbonx.service.DocumentService;
import com.ecapybara.carbonx.service.GraphService;
import com.ecapybara.carbonx.service.arango.ArangoDatabaseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {
  
    @Autowired
    private ArangoDatabaseService databaseService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private GraphService graphService;
    @Autowired
    private ProductRepository productRepository;

    final Sort sort = Sort.by(Direction.DESC, "id");


    @GetMapping
    public ResponseEntity<Object> listAllProducts(@RequestParam(required = false, defaultValue = "global") String targetDatabase) {
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
            return new ResponseEntity<>(result, HttpStatus.OK);

        } else if (databases.contains(targetDatabase)) { // returns whatever is in the specified database name
            response = documentService.getAllDocuments(targetDatabase, "products").block();
            result = mapper.convertValue(response.get("result"), new TypeReference<List<Product>>() {});
            return new ResponseEntity<>(result, HttpStatus.OK);

        } else { // throws error because database is confirmed to be unidentified
            return new ResponseEntity<>(documentService.getAllDocuments(targetDatabase, "products").block(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{companyName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createProducts(@PathVariable String companyName, @RequestBody List<Object> rawProducts) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> productList = mapper.convertValue(rawProducts, new TypeReference<List<Product>>() {});
            List<Object> response = documentService.createDocuments(companyName, "products", productList, true, null, null, null, null).block();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e)  {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{companyName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editProducts(@PathVariable String companyName, @RequestBody List<Object> revisedProducts) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Product> productList = mapper.convertValue(revisedProducts, new TypeReference<List<Product>>() {});
            List<Object> response = documentService.updateDocuments(companyName, "products", productList, true, true, null, null, true, null).block();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e)  {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{companyName}/{key}")
    public ResponseEntity<Object> getProduct(@PathVariable String companyName, @PathVariable String key) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> rawDocument = documentService.getDocument(companyName, "products", key, null, null).block();
            Product product = mapper.convertValue(rawDocument, new TypeReference<Product>() {});
            return new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e)  {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{companyName}/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editProduct(@RequestBody Map<String,Object> revisedProducts) {
        for (Product productRevision : revisedProducts) {
        Product product = editProduct(productRevision.getId(), productRevision);
        productRevision = product; //replace the list element with the new entity from database
        }
        return revisedProducts;
    }

    // Proper document deletion require the use of ArangoDB's Graph API since AQL does not cleanly delete hanging edges. Trust me, I've tried
    @DeleteMapping("/{companyName}/{key}")
    public Mono<Boolean> deleteProduct(@PathVariable String id) {
        return graphService.deleteDocuments("products", id);
    }

    /*
    @GetMapping("/{id}/calculate")
    public Mono<?> calculateProduct(@PathVariable String id) {
    Product product = productRepository.findById(id).orElse(null);
    return LCAService.calculate(product);
    }
    */
}