package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.ecapybara.carbonx.model.ipcc.EmissionChart;
import com.opencsv.bean.CsvRecurse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class Scope2 extends DetailedChart{
  @CsvRecurse
  private EmissionChart purchasedElectricity;

  @CsvRecurse
  private EmissionChart purchasedSteam;

  @CsvRecurse
  private EmissionChart purchasedHeating;

  @CsvRecurse
  private EmissionChart purchasedCooling;

  public Scope2() {
    super("indirect emissions");
  }
}
