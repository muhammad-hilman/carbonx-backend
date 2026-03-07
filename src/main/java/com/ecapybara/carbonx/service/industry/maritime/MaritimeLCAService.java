package com.ecapybara.carbonx.service.industry.maritime;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecapybara.carbonx.service.arango.ArangoQueryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MaritimeLCAService {
    @Autowired
    private ArangoQueryService queryService;
    
    public Map<String,Object> calculateRoughCarbonFootprint(String database, String key) {
        Map<String,Double> travelEmissions = this.calculateTravelEmissions(database, key);
        return null;
    }

    private Map<String,Double> calculateTravelEmissions(String database, String key) { //e.g {"kgCO2e":3456}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

        // Get the array of timestamps using AQL
        String query = "";
        Map<String, String> bindVars = Map.of(  "startNode", null,
                                                "graphName", null);

        List<String> timestamps = (List<String>) queryService.executeQuery(database, query, bindVars, 100, null, null, null).block().get("result");

        // Convert List<String> into List<ZonedDateTime>
        List<ZonedDateTime> times = timestamps.stream()
            .map(s -> ZonedDateTime.parse(s, formatter))
            .toList();

        // Determine the earliest and latest timestamp
        ZonedDateTime min = times.stream().min(ZonedDateTime::compareTo).orElseThrow();
        ZonedDateTime max = times.stream().max(ZonedDateTime::compareTo).orElseThrow();

        // Determine the elapsed duration
        Duration elapsed = Duration.between(min, max);
        long seconds = elapsed.getSeconds();

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        System.out.printf("Elapsed: %d hours %d minutes %d seconds%n", hours, minutes, secs);
        return null;
    }
}
