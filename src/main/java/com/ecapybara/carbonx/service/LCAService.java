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

    public <T extends Node> Mono<T> calculateDetailedCarbonFootprint(T node, String graphName) {

        String query = "FOR v, e, p IN 1..1000 INBOUND @startNode GRAPH @graphName \n" +
                "OPTIONS { bfs: true, uniqueVertices: 'global' } \n" +
                "LET s1 = v.dpp.carbonFootprint.scope1.kgCO2e == 0 OR v.dpp.carbonFootprint.scope1.kgCO2e == null \n" +
                "    ? ( \n" +
                "        (v.emissionInformation.scope1.stationaryCombustion.CO2.kg  * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope1.mobileCombustion.CH4.kg      * DOCUMENT('globalWarmingPotentials/CH4').gwp) + \n" +
                "        (v.emissionInformation.scope1.fugitiveEmissions.N2O.kg     * DOCUMENT('globalWarmingPotentials/N2O').gwp) + \n" +
                "        (v.emissionInformation.scope1.processEmissions.CO2.kg      * DOCUMENT('globalWarmingPotentials/CO2').gwp) \n" +
                "      ) \n" +
                "    : v.dpp.carbonFootprint.scope1.kgCO2e \n" +
                "LET s2 = v.dpp.carbonFootprint.scope2.kgCO2e == 0 OR v.dpp.carbonFootprint.scope2.kgCO2e == null \n" +
                "    ? ( \n" +
                "        (v.emissionInformation.scope2.purchasedElectricity.CO2.kg  * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope2.purchasedSteam.HFC134a.kg    * DOCUMENT('globalWarmingPotentials/HFC134a').gwp) + \n" +
                "        (v.emissionInformation.scope2.purchasedHeating.CO2.kg      * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope2.purchasedCooling.CO2.kg      * DOCUMENT('globalWarmingPotentials/CO2').gwp) \n" +
                "      ) \n" +
                "    : v.dpp.carbonFootprint.scope2.kgCO2e \n" +
                "LET s3 = v.dpp.carbonFootprint.scope3.kgCO2e == 0 OR v.dpp.carbonFootprint.scope3.kgCO2e == null \n" +
                "    ? ( \n" +
                "        (v.emissionInformation.scope3.category1.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category2.CH4.kg     * DOCUMENT('globalWarmingPotentials/CH4').gwp) + \n" +
                "        (v.emissionInformation.scope3.category3.N2O.kg     * DOCUMENT('globalWarmingPotentials/N2O').gwp) + \n" +
                "        (v.emissionInformation.scope3.category4.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category5.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category6.SF6.kg     * DOCUMENT('globalWarmingPotentials/SF6').gwp) + \n" +
                "        (v.emissionInformation.scope3.category7.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category8.CH4.kg     * DOCUMENT('globalWarmingPotentials/CH4').gwp) + \n" +
                "        (v.emissionInformation.scope3.category9.N2O.kg     * DOCUMENT('globalWarmingPotentials/N2O').gwp) + \n" +
                "        (v.emissionInformation.scope3.category10.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category11.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category12.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category13.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category14.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "        (v.emissionInformation.scope3.category15.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) \n" +
                "      ) \n" +
                "    : v.dpp.carbonFootprint.scope3.kgCO2e \n" +
                "COLLECT AGGREGATE \n" +
                "    rawS1 = SUM(s1), \n" +
                "    rawS2 = SUM(s2), \n" +
                "    rawS3 = SUM(s3) \n" +
                "RETURN { \n" +
                "    s1: ROUND(rawS1 * 100) / 100.0, \n" +
                "    s2: ROUND(rawS2 * 100) / 100.0, \n" +
                "    s3: ROUND(rawS3 * 100) / 100.0 \n" +
                "}";

        Map<String, String> bindVars = Map.of(
                "startNode", node.getId(),
                "graphName", graphName);

        return queryService.executeQuery("default", query, bindVars, 100, null, null, null)
                .map(r -> ((ArrayList<Map<String, Object>>) r.get("result")).get(0))
                .map(result -> {
                    double s1 = ((Number) result.get("s1")).doubleValue();
                    double s2 = ((Number) result.get("s2")).doubleValue();
                    double s3 = ((Number) result.get("s3")).doubleValue();

                    log.info("Detailed traversal totals -> s1={}, s2={}, s3={}", s1, s2, s3);
                    log.info("Prior DPP -> {}", node.getDPP());

                    node.getDPP().getCarbonFootprint().setScope1(Map.of("kgCO2e", s1));
                    node.getDPP().getCarbonFootprint().setScope2(Map.of("kgCO2e", s2));
                    node.getDPP().getCarbonFootprint().setScope3(Map.of("kgCO2e", s3));

                    log.info("New DPP -> {}", node.getDPP());
                    return node;
                });
    }
 public <T extends Node> Mono<T> calculateEmissionInformation(T node, String graphName) {

        String query =
                "LET ei = DOCUMENT(@startNode).emissionInformation \n" +
                "LET s1 = ( \n" +
                "    (ei.scope1.stationaryCombustion.CO2.kg  * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope1.mobileCombustion.CH4.kg      * DOCUMENT('globalWarmingPotentials/CH4').gwp) + \n" +
                "    (ei.scope1.fugitiveEmissions.N2O.kg     * DOCUMENT('globalWarmingPotentials/N2O').gwp) + \n" +
                "    (ei.scope1.processEmissions.CO2.kg      * DOCUMENT('globalWarmingPotentials/CO2').gwp) \n" +
                ") \n" +
                "LET s2 = ( \n" +
                "    (ei.scope2.purchasedElectricity.CO2.kg  * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope2.purchasedSteam.HFC134a.kg    * DOCUMENT('globalWarmingPotentials/HFC134a').gwp) + \n" +
                "    (ei.scope2.purchasedHeating.CO2.kg      * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope2.purchasedCooling.CO2.kg      * DOCUMENT('globalWarmingPotentials/CO2').gwp) \n" +
                ") \n" +
                "LET s3 = ( \n" +
                "    (ei.scope3.category1.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category2.CH4.kg     * DOCUMENT('globalWarmingPotentials/CH4').gwp) + \n" +
                "    (ei.scope3.category3.N2O.kg     * DOCUMENT('globalWarmingPotentials/N2O').gwp) + \n" +
                "    (ei.scope3.category4.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category5.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category6.SF6.kg     * DOCUMENT('globalWarmingPotentials/SF6').gwp) + \n" +
                "    (ei.scope3.category7.CO2.kg     * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category8.CH4.kg     * DOCUMENT('globalWarmingPotentials/CH4').gwp) + \n" +
                "    (ei.scope3.category9.N2O.kg     * DOCUMENT('globalWarmingPotentials/N2O').gwp) + \n" +
                "    (ei.scope3.category10.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category11.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category12.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category13.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category14.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) + \n" +
                "    (ei.scope3.category15.CO2.kg    * DOCUMENT('globalWarmingPotentials/CO2').gwp) \n" +
                ") \n" +
                "RETURN { \n" +
                "    s1: ROUND(s1 * 100) / 100.0, \n" +
                "    s2: ROUND(s2 * 100) / 100.0, \n" +
                "    s3: ROUND(s3 * 100) / 100.0 \n" +
                "}";

        Map<String, String> bindVars = Map.of("startNode", node.getId());

        return queryService.executeQuery("default", query, bindVars, 100, null, null, null)
                .map(r -> ((ArrayList<Map<String, Object>>) r.get("result")).get(0))
                .map(result -> {
                    double s1 = ((Number) result.get("s1")).doubleValue();
                    double s2 = ((Number) result.get("s2")).doubleValue();
                    double s3 = ((Number) result.get("s3")).doubleValue();

                    log.info("Emission info kgCO2e -> s1={}, s2={}, s3={}", s1, s2, s3);
                    log.info("Prior DPP -> {}", node.getDPP());

                    node.getDPP().getCarbonFootprint().setScope1(Map.of("kgCO2e", s1));
                    node.getDPP().getCarbonFootprint().setScope2(Map.of("kgCO2e", s2));
                    node.getDPP().getCarbonFootprint().setScope3(Map.of("kgCO2e", s3));

                    log.info("New DPP -> {}", node.getDPP());
                    return node;
                });
    }

}