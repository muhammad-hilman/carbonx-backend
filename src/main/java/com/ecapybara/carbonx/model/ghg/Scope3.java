package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.ecapybara.carbonx.model.ipcc.EmissionChart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class Scope3 extends DetailedChart {
  private EmissionChart category_1;
  private EmissionChart category_2;
  private EmissionChart category_3;
  private EmissionChart category_4;
  private EmissionChart category_5;
  private EmissionChart category_6;
  private EmissionChart category_7;
  private EmissionChart category_8;
  private EmissionChart category_9;
  private EmissionChart category_10;
  private EmissionChart category_11;
  private EmissionChart category_12;
  private EmissionChart category_13;
  private EmissionChart category_14;
  private EmissionChart category_15;

  public Scope3() {
    super("direct and indirect emissions");
  }
}
