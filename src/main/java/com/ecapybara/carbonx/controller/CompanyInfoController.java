package com.ecapybara.carbonx.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.model.basic.Company;
import com.ecapybara.carbonx.model.issb.Product;
import com.ecapybara.carbonx.service.arango.ArangoCollectionService;
import com.ecapybara.carbonx.service.arango.ArangoDatabaseService;
import com.ecapybara.carbonx.service.arango.ArangoDocumentService;
import com.ecapybara.carbonx.service.arango.ArangoGraphService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/info")
public class CompanyInfoController {

    @Autowired
    private ArangoDatabaseService databaseService;
    @Autowired
    private ArangoCollectionService collectionService;
    @Autowired
    private ArangoGraphService graphService;
    @Autowired
    private ArangoDocumentService documentService;

    final Sort sort = Sort.by(Direction.DESC, "id");

    // UNFINISHED
    @GetMapping
    public String listCompanyNames() {
        return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createCompany(@RequestBody Map<String,Object> companyInfo) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (companyInfo.containsKey("name") && companyInfo.containsKey("sector")) {
                Company company = mapper.convertValue(companyInfo, new TypeReference<Company>() {});

                documentService.createDocument("default", "companies", company, null, null, null, null, null).block();

                String companyName = company.getName();
                String companySector = company.getSector();
                databaseService.createDatabase(companyName, null, null, null, null).block();
                collectionService.createCollection(companyName, "users", 2, true, null, null, null, null).block();
                collectionService.createCollection(companyName, "relevantMetrics", 2, true, null, null, null, null).block();
                collectionService.createCollection(companyName, "products", 2, true, null, null, null, null).block();
                collectionService.createCollection(companyName, "processes", 2, true, null, null, null, null).block();
                collectionService.createCollection(companyName, "inputs", 3, true, null, null, null, null).block();
                collectionService.createCollection(companyName, "outputs", 3, true, null, null, null, null).block();

                Map<String,Object> inputs = Map.of( "collection", "inputs",
                                                    "from", List.of("products"),
                                                    "to", List.of("processes"));
                Map<String,Object> outputs = Map.of("collection", "outputs",
                                                    "from", List.of("processes"),
                                                    "to", List.of("products"));
                graphService.createGraph(companyName, "default", List.of(inputs, outputs), null, null, null, null).block();
                
                switch (companySector) {
                    case "maritime":
                        collectionService.createCollection(companyName, "ships", 2, true, null, null, null, null).block();
                        collectionService.createCollection(companyName, "shipLogs", 2, true, null, null, null, null).block();
                    default:
                        return new ResponseEntity<>(company, HttpStatus.OK);
                }
            } else {
                throw new IllegalArgumentException();
            }            
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("error", "Illegal 'name' and/or 'sector' values!"), HttpStatus.BAD_REQUEST);
        }        
    }

    // Unfinished
    @GetMapping("/{companyName}")
    public Object getCompanyInfo() {
    return null;
    }

    // UNFINISHED
    @GetMapping("/metrics")
    public List<String> getCompanyMetrics() {
    return null;
    }

    // UNFINISHED
    @GetMapping("/sector")
    public String getCompanySector() {
    return null;
    }

    // UNFINISHED
    @GetMapping("/industry")
    public String getCompanyIndustry() {
    return null;
    }
}