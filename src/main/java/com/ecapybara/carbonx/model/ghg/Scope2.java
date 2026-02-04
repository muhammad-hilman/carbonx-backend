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
public class Scope2 extends DetailedChart{
  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.purchasedElectricity.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> purchasedElectricity = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.purchasedSteam.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> purchasedSteam = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.purchasedHeating.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> purchasedHeating = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.purchasedCooling.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> purchasedCooling = null;

  public Scope2() {
    super("indirect emissions");
  }
}
