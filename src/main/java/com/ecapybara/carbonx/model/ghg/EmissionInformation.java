package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class EmissionInformation extends DetailedChart{
  private Scope1 scope_1;
  private Scope2 scope_2;
  private Scope3 scope_3;

  public EmissionInformation() {
    super("GHG");
  }
}
