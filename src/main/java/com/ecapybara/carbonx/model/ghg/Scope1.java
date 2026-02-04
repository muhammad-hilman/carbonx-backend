package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.ecapybara.carbonx.model.ipcc.EmissionChart;
import com.opencsv.bean.CsvRecurse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class Scope1 extends DetailedChart {
  @CsvRecurse
  private EmissionChart stationaryCombustion;

  @CsvRecurse
  private EmissionChart mobileCombustion;

  @CsvRecurse
  private EmissionChart fugitiveEmissions;

  @CsvRecurse
  private EmissionChart processEmissions;

  public Scope1() {
    super("direct emissions");
  }
}
