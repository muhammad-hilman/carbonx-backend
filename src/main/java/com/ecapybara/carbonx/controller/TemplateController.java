package com.ecapybara.carbonx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.service.TemplateService;
import com.ecapybara.carbonx.service.arango.ArangoQueryService;

@Slf4j
@RestController
@RequestMapping("/api/templates")

public class TemplateController {
    @Autowired
    private TemplateService graphService;
    @Autowired
    private ArangoQueryService queryService;

    // Get the entire network graph and assembles the data into the JSON-D3 format that frontend can use
   @GetMapping
    public Object listTemplates(@RequestParam(defaultValue = "default") String database,
                                @RequestParam(defaultValue = "all") String nodeType) {

        Map<String,?> result;
        switch (nodeType) {
            case "low":
                // Fetch low-level nodes
                Collection<String> lowProducts = graphService.listLeafNodes(database, "products");
                Collection<String> lowProcesses = graphService.listLeafNodes(database, "processes");

                // Send Map<String,Collection<String>>
                result = Map.of("products", lowProducts,
                                "processes", lowProcesses);
                log.info("Identified leaf nodes -> {}", result);
                return result;

            case "intermediate":
                // Fetch intermediate-level nodes
                Collection<String> intermediateProducts = graphService.listIntermediateNodes(database, "products");
                Collection<String> intermediateProcesses = graphService.listIntermediateNodes(database, "processes");

                // Send Map<String,Collection<String>>
                result = Map.of("products", intermediateProducts,
                                "processes", intermediateProcesses);
                log.info("Identified intermediate nodes -> {}", result);
                return result;

            case "high":
                // Fetch high-level nodes
                Collection<String> highProducts = graphService.listRootNodes(database, "products");
                Collection<String> highProcesses = graphService.listRootNodes(database, "processes");

                // Send Map<String,Collection<String>>
                result = Map.of("products", highProducts,
                                "processes", highProcesses);
                log.info("Identified root nodes -> {}", result);
                return result;

            case "all":
                // Fetch high-level nodes
                Collection<String> allProducts = graphService.listAllNodes(database, "products");
                Collection<String> allProcesses = graphService.listAllNodes(database, "processes");

                // Send Map<String,Collection<String>>
                result = Map.of("products", allProducts,
                                "processes", allProcesses);
                log.info("Identified all nodes -> {}", result);
                return result;

            default:
                return "Error identifying 'nodeType' request parameter";            
        }
    }

    // This fetches the specific product template and assembles the data into the JSON-D3 format that frontend can use
    @GetMapping("/{collection}/{key}")
    public Map<String,ArrayList<Map<String,String>>> getTemplate(@RequestParam(defaultValue = "default") String database, 
                                                                 @PathVariable String collection,
                                                                 @PathVariable String key) {

        String query =  "LET nodes = ( \r\n" +
                        "    FOR v, e, p IN 1..100 INBOUND @startNode GRAPH default \r\n" +
                        "        COLLECT id = v._id, label = v.name, type = PARSE_IDENTIFIER(v._id).collection \r\n" +
                        "        RETURN { id, label, type }) \r\n" +
                        "LET edges = ( \r\n" +
                        "    FOR v, e, p IN 1..100 INBOUND @startNode GRAPH default \r\n" +
                        "        FILTER e != null \r\n" +
                        "        COLLECT source = e._from, target = e._to, type = PARSE_IDENTIFIER(e._id).collection \r\n" +
                        "        RETURN { source, target, type }) \r\n" +
                        "RETURN { nodes, edges }";

        Map<String, String> bindVars = Map.of("startNode", collection+"/"+key);
        log.info("bindvars -> {}", bindVars);
        ArrayList<Map<String,ArrayList<Map<String,String>>>> response = (ArrayList<Map<String,ArrayList<Map<String,String>>>>) queryService.executeQuery(query, bindVars, 100, null, null, null).block().get("result");
        log.info("response -> {}", response);

        return response.get(0);
    }
}
