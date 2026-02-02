package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.ecapybara.carbonx.model.ipcc.EmissionChart;
import com.opencsv.bean.CsvRecurse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class Scope3 extends DetailedChart {
  @CsvRecurse
  private EmissionChart category_1;

  @CsvRecurse
  private EmissionChart category_2;

  @CsvRecurse
  private EmissionChart category_3;

  @CsvRecurse
  private EmissionChart category_4;

  @CsvRecurse
  private EmissionChart category_5;

  @CsvRecurse
  private EmissionChart category_6;

  @CsvRecurse
  private EmissionChart category_7;

  @CsvRecurse
  private EmissionChart category_8;

  @CsvRecurse
  private EmissionChart category_9;

  @CsvRecurse
  private EmissionChart category_10;

  @CsvRecurse
  private EmissionChart category_11;

  @CsvRecurse
  private EmissionChart category_12;

  @CsvRecurse
  private EmissionChart category_13;

  @CsvRecurse
  private EmissionChart category_14;

  @CsvRecurse
  private EmissionChart category_15;

  public Scope3() {
    super("direct and indirect emissions");
  }
}
