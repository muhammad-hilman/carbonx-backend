package com.ecapybara.carbonx.model.ghg;

import java.util.Map;

import com.ecapybara.carbonx.utils.csv.ComplexMapConverter;
import com.opencsv.bean.CsvCustomBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class Scope3 {
  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category1", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category1 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category2", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category2 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category3", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category3 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category4", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category4 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category5", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category5 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category6", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category6 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category7", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category7 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category8", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category8 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category9", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category9 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category10", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category10 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category11", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category11 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category12", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category12 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category13", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category13 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category14", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category14 = null;

  @Builder.Default
  @CsvCustomBindByName(column = "emissionInformation.scope2.category15", converter = ComplexMapConverter.class)
  private Map<String, Map<String, Double>> category15 = null;
}
