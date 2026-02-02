package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.ecapybara.carbonx.model.ipcc.EmissionChart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class Scope2 extends DetailedChart{
  private EmissionChart purchasedElectricity;
  private EmissionChart purchasedSteam;
  private EmissionChart purchasedHeating;
  private EmissionChart purchasedCooling;

  public Scope2() {
    super("indirect emissions");
  }
}
