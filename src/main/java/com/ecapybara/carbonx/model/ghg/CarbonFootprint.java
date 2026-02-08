package com.ecapybara.carbonx.model.ghg;

import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class CarbonFootprint {
  @CsvBindByName(column = "dpp.carbonFootprint.scope1")
  private Map<String, Double> scope1; //e.g {"kgCO2e": 20.2}

  @CsvBindByName(column = "dpp.carbonFootprint.scope2")
  private Map<String, Double> scope2; //e.g {"kgCO2e": 20.2}

  @CsvBindAndJoinByName(column = "dpp.carbonFootprint.scope3.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> scope3; //eg. {"Category 1": metricObject, "Category 2": metricObject}
}
