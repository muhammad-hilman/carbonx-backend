package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.ecapybara.carbonx.model.ipcc.EmissionChart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class Scope1 extends DetailedChart {
  private EmissionChart stationaryCombustion;
  private EmissionChart mobileCombustion;
  private EmissionChart fugitiveEmissions;
  private EmissionChart processEmissions;

  public Scope1() {
    super("direct emissions");
  }
}
