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

import com.ecapybara.carbonx.service.GraphService;
import com.ecapybara.carbonx.service.arango.ArangoQueryService;

@Slf4j
@RestController
@RequestMapping("/api/graph")

public class GraphController {
    @Autowired
    private GraphService graphService;

    // Get the entire network graph and assembles the data into the JSON-D3 format that frontend can use
   @GetMapping
    public Object listGraphs(@RequestParam(required = true) String database,
                             @RequestParam(defaultValue = "all") String nodeType) {

        Map<String,?> result;
        switch (nodeType) {
            case "low":
                // Fetch low-level nodes
                Collection<String> lowProducts = graphService.getLeafNodes("default", "products");
                Collection<String> lowProcesses = graphService.getLeafNodes("default", "processes");

                // Send Map<String,Collection<String>>
                result = Map.of("products", lowProducts,
                                "processes", lowProcesses);
                log.info("Identified leaf nodes -> {}", result);
                return result;

            case "intermediate":
                // Fetch intermediate-level nodes
                Collection<String> intermediateProducts = graphService.getIntermediateNodes("default", "products");
                Collection<String> intermediateProcesses = graphService.getIntermediateNodes("default", "processes");

                // Fetch sub-product and processes
                for (String product: intermediateProducts) {

                }

                // Send Map<String,Collection<String>>

            case "high":
                // Fetch high-level nodes

                // Fetch sub-product and processes

                // Send Map<String,Collection<String>>


            case "all":
                // Fetch all nodes

                // Fetch sub-product and processes

                // Send Map<String,Collection<String>>

            default:
                return "Missing Parameter";            
        }

        List<String> vertexCollections = graphService.getNodeCollections(graphname);
        List<String> edgeCollections = graphService.getEdgeCollections(graphname);
        StringBuilder query = new StringBuilder();

        // ------------------ NODES ------------------
        query.append("LET nodes = UNIQUE(FLATTEN([");

        for (int i = 0; i < vertexCollections.size(); i++) {
            String col = vertexCollections.get(i);

            query.append("(")
                .append("FOR v IN ").append(col).append(" ")
                .append("RETURN { ")
                .append("id: v._id, ")
                .append("name: HAS(v, \"name\") ? v.name : v._key, ")
                .append("type: \"").append(col).append("\" ")
                .append("}")
                .append(")");

            if (i < vertexCollections.size() - 1) {
                query.append(",");
            }
        }

        query.append("])) ");

        // ------------------ LINKS ------------------
        query.append("LET links = UNIQUE(FLATTEN([");

        for (int i = 0; i < edgeCollections.size(); i++) {
            String col = edgeCollections.get(i);

            query.append("(")
                .append("FOR e IN ").append(col).append(" ")
                .append("RETURN { ")
                .append("source: e._from, ")
                .append("target: e._to, ")
                .append("type: \"").append(col).append("\" ")
                .append("}")
                .append(")");

            if (i < edgeCollections.size() - 1) {
                query.append(",");
            }
        }

        query.append("])) ");

        query.append("RETURN { nodes: nodes, links: links }");

        System.out.println(query.toString());  // DEBUG

        Map<String, Object> response =
            graphService.executeQuery(database, query.toString());

        return response.get("result");
    }

    // This fetches the specific product graph and assembles the data into the JSON-D3 format that frontend can use
    @GetMapping
    public Object getGraph(@RequestParam String database, @RequestParam String id) {
        //currently hardcoded to use products,processes,inputs and outputs, can be improved later
        String targetNode = "products/"+productid;
        String edgeList = "inputs, outputs";

        String query =
        "LET targetNode = \"" + targetNode + "\" " +

        "LET traversal = ( " +
        "FOR v, e, p IN 1..100 INBOUND targetNode " +
        edgeList + " " +
        "FILTER e != null " +
        "RETURN { " +
            "node: { " +
                "id: v._id, " +
                "label: HAS(v, \"name\") ? v.name : v._key, " +
                "type: PARSE_IDENTIFIER(v._id).collection " +
            "}, " +
            "edge: { " +
                "source: e._from, " +
                "target: e._to, " +
                "type: PARSE_IDENTIFIER(e._id).collection " +
            "} " +
        "} " +
        ") " +

        "LET nodes = UNIQUE(traversal[* RETURN CURRENT.node]) " +
        "LET links = UNIQUE(traversal[* RETURN CURRENT.edge]) " +

        "LET rootNode = { " +
            "id: targetNode, " +
            "label: HAS(DOCUMENT(targetNode), \"name\") " +
                "? DOCUMENT(targetNode).name " +
                ": PARSE_IDENTIFIER(targetNode).key, " +
            "type: PARSE_IDENTIFIER(targetNode).collection " +
        "} " +

        "RETURN { nodes: APPEND(nodes, rootNode, true), links: links }";

        Map<String, Object> response = graphService.executeQuery(database, query);

        return response.get("result");
    }


}
