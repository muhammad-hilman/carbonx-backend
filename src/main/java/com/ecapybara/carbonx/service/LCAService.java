package com.ecapybara.carbonx.service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecapybara.carbonx.model.basic.Node;
import com.ecapybara.carbonx.service.arango.ArangoQueryService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LCAService {

  @Autowired
  private ArangoQueryService queryService;

    public <T extends Node> Mono<T> calculateRoughCarbonFootprint(T node, String graphName) {

        String query = "FOR v, e, p IN 1..1000 INBOUND @startNode GRAPH @graphName \n" +
                "OPTIONS { bfs: true, uniqueVertices: 'global' } \n" +
                "COLLECT AGGREGATE \n" +
                "  rawS1 = SUM(v.dpp.carbonFootprint.scope1.kgCO2e), \n" +
                "  rawS2 = SUM(v.dpp.carbonFootprint.scope2.kgCO2e), \n" +
                "  rawS3 = SUM(v.dpp.carbonFootprint.scope3.kgCO2e) \n" +
                "LET s1 = ROUND(rawS1 * 100) / 100.0, \n" +
                "    s2 = ROUND(rawS2 * 100) / 100.0, \n" +
                "    s3 = ROUND(rawS3 * 100) / 100.0 \n" +
                "RETURN {s1, s2, s3}";

        Map<String, String> bindVars = Map.of(
                "startNode", node.getId(),
                "graphName", graphName);

        return queryService.executeQuery("default", query, bindVars, 100, null, null, null)
                .map(r -> ((ArrayList<Map<String, Object>>) r.get("result")).get(0))
                .map(result -> {
                    double s1 = ((Number) result.get("s1")).doubleValue();
                    double s2 = ((Number) result.get("s2")).doubleValue();
                    double s3 = ((Number) result.get("s3")).doubleValue();

                    log.info("Traversal totals -> s1={}, s2={}, s3={}", s1, s2, s3);
                    log.info("Prior DPP -> {}", node.getDPP());

                    node.getDPP().getCarbonFootprint().setScope1(Map.of("kgCO2e", s1));
                    node.getDPP().getCarbonFootprint().setScope2(Map.of("kgCO2e", s2));
                    node.getDPP().getCarbonFootprint().setScope3(Map.of("kgCO2e", s3));

                    log.info("New DPP -> {}", node.getDPP());
                    return node;
                });
    }

    // ---- Unfinished
    public <T extends Node> Mono<T> calculateDetailedCarbonFootprint(T node, String graphName) {
        return Mono.empty();
    }

    // ---- Unfinished
    public <T extends Node> Mono<T> calculateEmissionInformation(T node, String graphName) {
        return Mono.empty();
    }
}