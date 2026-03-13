package com.ecapybara.carbonx.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecapybara.carbonx.model.issb.Input;
import com.ecapybara.carbonx.model.issb.Product;
import com.ecapybara.carbonx.service.ImportExportService;
import com.ecapybara.carbonx.service.arango.ArangoDatabaseService;
import com.ecapybara.carbonx.service.arango.ArangoDocumentService;
import com.ecapybara.carbonx.service.arango.ArangoGraphService;
import com.ecapybara.carbonx.service.arango.ArangoQueryService;
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
   private ArangoDocumentService documentService;
   @Autowired
   private ArangoGraphService graphService;
   @Autowired
   private ArangoQueryService queryService;
   @Autowired
   private ObjectMapper objectMapper;

   final Sort sort = Sort.by(Direction.DESC, "id");
   private static final String ARANGO_DB = "default";
   private static final String ARANGO_GRAPH = "default";
   private static final String COLLECTION = "products";

    //gives all products
    @GetMapping
    public List<Product> searchAllProducts(@RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

    String aql = "for p in "+COLLECTION+" return p";

    Map<String, Object> response = queryService.executeQuery(targetDatabase, aql, null, null, null, null, null).block();

    if (response == null || !response.containsKey("result")) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "No products found in database: " + targetDatabase);
    }

    return objectMapper.convertValue(response.get("result"), new TypeReference<List<Product>>() {});
    }

    //get product by key
    @GetMapping("/{key}")
    public Product getProduct(@PathVariable String key,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        Map response = documentService.getDocument(COLLECTION, key, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + key);
        }

        return objectMapper.convertValue(response, Product.class);
    }

    //for a certain product identified by key, get value of specifed fields
    @GetMapping("/{key}/field")
    public Map<String, Object> getProductFields(
        @PathVariable String key,
        @RequestParam List<String> fieldName,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        String fields = fieldName.stream()
            .map(f -> "\"" + f + "\": p." + f)
            .collect(Collectors.joining(", "));

        String aql = "FOR p IN " + COLLECTION + " FILTER p._key == @key RETURN {" + fields + "}";
        Map<String, String> bindVars = new HashMap<>();
        bindVars.put("key", key);

        Map<String, Object> response = queryService.executeQuery(targetDatabase, aql, bindVars, null, null, null, null).block();

        if (response == null || !response.containsKey("result")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product or field not found");
        }

        List<Map<String, Object>> result = (List<Map<String, Object>>) response.get("result");
        return result.isEmpty() ? null : result.get(0);
    }


    //get list of products that match search criterias
    @GetMapping("/search")
    public List<Product> searchProducts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String productOrigin,
        @RequestParam(required = false) String userId,
        @RequestParam(required = false) String quantifiableUnit,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        StringBuilder aql = new StringBuilder("FOR p IN " + COLLECTION + " FILTER ");
        Map<String, String> bindVars = new HashMap<>();
        List<String> filters = new ArrayList<>();

        if (name != null)             { filters.add("p.name == @name");                         bindVars.put("name", name); }
        if (type != null)             { filters.add("p.type == @type");                         bindVars.put("type", type); }
        if (productOrigin != null)    { filters.add("p.productOrigin == @productOrigin");       bindVars.put("productOrigin", productOrigin); }
        if (userId != null)           { filters.add("p.userId == @userId");                     bindVars.put("userId", userId); }
        if (quantifiableUnit != null) { filters.add("p.quantifiableUnit == @quantifiableUnit"); bindVars.put("quantifiableUnit", quantifiableUnit); }

        if (filters.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one search criteria is required");
        }

        aql.append(String.join(" AND ", filters)).append(" RETURN p");

        Map<String, Object> response = queryService.executeQuery(targetDatabase, aql.toString(), bindVars, null, null, null, null).block();

        if (response == null || !response.containsKey("result")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No products found");
        }

        return objectMapper.convertValue(response.get("result"), new TypeReference<List<Product>>() {});
    }

    /*
    create a products base on list in json body
    [
    {
        "type": "ingredient",
        "name": "onion juice",
        "productOrigin": "newsupplier",
        "userId": "user123"
    },
    {
        "type": "ingredient",
        "name": "lemon juice",
        "productOrigin": "newsupplier",
        "userId": "user123"
    }
    
    
    ]
    */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Product> createProducts(
        @RequestBody List<Product> productsList,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        List<Object> documents = productsList.stream()
            .map(product -> {
                Map<String, Object> doc = objectMapper.convertValue(product, new TypeReference<Map<String, Object>>() {});
                if (doc.get("_key") == null) doc.remove("_key");
                return (Object) doc;
            })
            .collect(Collectors.toList());

        List<Map> response = (List<Map>) documentService.createDocuments(targetDatabase, COLLECTION, documents, null, true, null, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create products");
        }

        return response.stream()
            .map(item -> objectMapper.convertValue(item.get("new"), Product.class))
            .collect(Collectors.toList());
    }

    /*
    Change mutiple product based on key,
    [
    {
        "_key": "12345",
        "name": "updated lemon juice"
    },
    {
        "_key": "67890",
        "name": "updated lemonade",
        "type": "drink"
    }
    ] 
    */

    @PutMapping
    public List<Product> editProducts(
        @RequestBody List<Map<String, Object>> revisedProducts,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        List<Object> documents = revisedProducts.stream()
            .map(doc -> {
                if (doc.get("_key") == null) doc.remove("_key");
                return (Object) doc;
            })
            .collect(Collectors.toList());

        List<Map> response = (List<Map>) documentService.updateDocuments(COLLECTION, documents, null, true, null, null, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update products");
        }

        return response.stream()
            .map(item -> objectMapper.convertValue(item.get("new"), Product.class))
            .collect(Collectors.toList());
    }


    /*
    edit 1 product for given key
    {
    "name": "updated lemon juice"
    }
    */

    @PutMapping("/{key}")
    public Product editProduct(
        @PathVariable String key,
        @RequestBody Map<String, Object> doc,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {
        doc.remove("_key");
        //remove the things you should not modify

        Map response = documentService.updateDocument(COLLECTION, key, doc, null, true, null, null, null, null, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + key);
        }

        return objectMapper.convertValue(response.get("new"), Product.class);
    }


    @DeleteMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(
        @PathVariable String key,
        @RequestParam(required = false, defaultValue = ARANGO_GRAPH) String graphName,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        Map response = graphService.deleteVertex(targetDatabase,graphName, COLLECTION, key, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + key);
        }
    }

    @DeleteMapping
    public Map<String, Object> deleteProducts(
        @RequestBody List<String> keys,
        @RequestParam(required = false, defaultValue = "default") String graphName,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        List<String> succeeded = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (String key : keys) {
            try {
                graphService.deleteVertex(targetDatabase, graphName, COLLECTION, key, null, null).block();
                succeeded.add(key);
            } catch (Exception e) {
                log.error("Failed to delete product: {}", key, e);
                failed.add(key);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("succeeded", succeeded);
        result.put("failed", failed);
        result.put("success", failed.isEmpty());
        return result;
    }
        

}

//   /*
//   @GetMapping("/{id}/calculate")
//   public Mono<?> calculateProduct(@PathVariable String id) {
//     Product product = productRepository.findById(id).orElse(null);
//     return LCAService.calculate(product);
//   }
//   */

//   // Experimental endpoint to call for backend import function for products
//    @PostMapping("/import")
//    public Mono<?> testImport() {
//       String dir = System.getProperty("user.dir");
//       Path filepath = Paths.get(dir,"temp");
      
//       List<String> files = List.of("templates.csv");
      
//       return Flux.fromIterable(files)
//          .flatMap(filename -> importService.importCSV( filepath.resolve(filename), "testCompany", "products"))
//          .then(Mono.just("Successfully imported JSON files!"))
//          .onErrorReturn("Import failed - check logs");
//    }
// }