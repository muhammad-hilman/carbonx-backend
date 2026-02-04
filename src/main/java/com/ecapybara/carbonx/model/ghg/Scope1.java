package com.ecapybara.carbonx.model.ghg;

import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.opencsv.bean.CsvBindAndJoinByName;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class Scope1 extends DetailedChart {
  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.stationaryCombustion.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> stationaryCombustion = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.mobileCombustion.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> mobileCombustion = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.mobileCombustion.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> fugitiveEmissions = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope1.mobileCombustion.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> processEmissions = null;

  public Scope1() {
    super("direct emissions");
  }
}
