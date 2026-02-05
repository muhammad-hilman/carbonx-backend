package com.ecapybara.carbonx.model.ghg;

import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;

import com.opencsv.bean.CsvBindAndJoinByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @AllArgsConstructor @SuperBuilder(toBuilder = true)
public class Scope1 {
  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.stationaryCombustion.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> stationaryCombustion = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.mobileCombustion.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> mobileCombustion = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.fugitiveEmissions.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> fugitiveEmissions = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.processEmissions.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> processEmissions = null;
}
