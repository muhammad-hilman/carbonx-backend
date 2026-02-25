package com.ecapybara.carbonx.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.arangodb.springframework.repository.query.ArangoQueryMethod;
import com.ecapybara.carbonx.config.AppLogger;
import com.ecapybara.carbonx.model.basic.Graph;
import com.ecapybara.carbonx.service.arango.ArangoQueryService;

import reactor.core.publisher.Mono;

@Service
public class GraphService {
  
  @Autowired
  private ArangoQueryService queryService;
  @Autowired
  private WebClient webClient;
  private static final Logger log = LoggerFactory.getLogger(AppLogger.class);

  public Mono<Map> createGraph(Graph graph) {    
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("name", graph.getName()); // Required
    requestBody.put("edgeDefinitions", graph.getEdgeDefinitions());  // Required
    /*
    requestBody.put("orphanCollections", orphanCollections);
    */

    return webClient.post()
            .uri("/gharial")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class);
  }

  public Mono<Boolean> deleteDocuments(String collection, String key) {
    return webClient.delete()
            .uri("/gharial/{graph}/vertex/{vertexCollection}/{key}", "default", collection, key)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (Boolean) response.get("removed"))
            .doOnSuccess(removed -> log.info("Vertex deleted: {}", removed));
  }

  public String getGraphMetadata(String name) {    
    return webClient
            .get()
            .uri("/gharial/{name}",name)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public List<String> getEdgeCollections(String name) {    
        Map<String, Object> response = webClient
            .get()
            .uri("/gharial/{name}/edge",name)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();
        return (List<String>) response.get("collections");
        }

    public List<String> getNodeCollections(String name) {   
        Map<String, Object> response = webClient
            .get()
            .uri("/gharial/{name}/vertex",name)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();
        return (List<String>) response.get("collections");
    }

    // unfinished - "database" variable not wired up properly
    public Collection<String> getLeafNodes(String database, String collection) {
        String query = "FOR v IN @@collection \r\n" + //
                        "FILTER LENGTH( \r\n" + //
                        "    FOR e IN 1..1 INBOUND v GRAPH default \r\n" + //
                        "    RETURN 1" + //
                        ") == 0 \r\n" + //
                        "RETURN v._id\r\n" + //
                        "";

        Map<String, String> bindVars = Map.of("@collection", collection);
        Collection<String> result = (Collection<String>) queryService.executeQuery(query, bindVars, 100, null, null, null).block().get("result");
        return result;
    }

    // unfinished - "database" variable not wired up properly
    public Collection<String> getRootNodes(String database, String collection) {
        String query = "FOR v IN @@collection \r\n" + //
                        "FILTER LENGTH( \r\n" + //
                        "    FOR e IN 1..1 OUTBOUND v GRAPH default \r\n" + //
                        "    RETURN 1" + //
                        ") == 0 \r\n" + //
                        "RETURN v._id\r\n" + //
                        "";

        Map<String, String> bindVars = Map.of("@collection", collection);
        Collection<String> result = (Collection<String>) queryService.executeQuery(query, bindVars, 100, null, null, null).block().get("result");
        return result;
    }

    // unfinished - "database" variable not wired up properly
    public Collection<String> getIntermediateNodes(String database, String collection) {
        String query = "FOR v IN @@collection \r\n" + //
                        "FILTER LENGTH( \r\n" + //
                        "    FOR e IN 1..1 INBOUND v GRAPH default \r\n" + //
                        "    RETURN 1" + //
                        ") > 0 AND LENGTH( \r\n" + //
                        "    FOR e IN 1..1 OUTBOUND v GRAPH default \r\n" + //
                        "    RETURN 1" + //
                        ") > 0 \r\n" +
                        "RETURN v._id\r\n" + //
                        "";

        Map<String, String> bindVars = Map.of("@collection", "products");
        Collection<String> result = (Collection<String>) queryService.executeQuery(query, bindVars, 100, null, null, null).block().get("result");
        return result;
    }

    public Map<String,ArrayList<String>> getComponentNodes(String nodeId) {
        String query =  "FOR v, e, p IN 1..1000 INBOUND @startNode GRAPH default \r\n" + //
                        "OPTIONS { bfs: true, uniqueVertices: 'global'} \r\n" + //
                        "FILTER LENGTH(FOR neighbor, edge IN 1..1 INBOUND v GRAPH @graphName RETURN 1) == 0 \r\n" + //
                        "COLLECT \r\n" + //
                        "  AGGREGATE \r\n" + //
                        "    rawS1 = SUM(v.DPP.carbonFootprint.scope1.kgCO2e),\r\n" + //
                        "    rawS2 = SUM(v.DPP.carbonFootprint.scope2.kgCO2e),\r\n" + //
                        "    rawS3 = SUM(v.DPP.carbonFootprint.scope3.kgCO2e)\r\n" + //
                        "LET \r\n" + //
                        "  s1 = ROUND(rawS1 * 100) / 100.0,\r\n" + //
                        "  s2 = ROUND(rawS2 * 100) / 100.0,\r\n" + //
                        "  s3 = ROUND(rawS3 * 100) / 100.0\r\n" + //
                        "RETURN {s1, s2, s3}\r\n" + //
                        "";
    }
}
