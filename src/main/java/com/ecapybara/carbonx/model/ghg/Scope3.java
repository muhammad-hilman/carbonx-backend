package com.ecapybara.carbonx.model.ghg;

import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;

import com.opencsv.bean.CsvBindAndJoinByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class Scope3 {
  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category1.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category1 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category2.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category2 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category3.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category3 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category4.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category4 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category5.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category5 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category6.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category6 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category7.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category7 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category8.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category8 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category9.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category9 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category10.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category10 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category11.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category11 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category12.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category12 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category13.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category13 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category14.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category14 = null;

  @Builder.Default
  @CsvBindAndJoinByName(column = "emissionInformation.scope2.category15.*", elementType = Map.class)
  private MultiValuedMap<String, Map<String, Double>> category15 = null;
}
