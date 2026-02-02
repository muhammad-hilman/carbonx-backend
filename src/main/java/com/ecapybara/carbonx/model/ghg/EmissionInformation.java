package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.opencsv.bean.CsvRecurse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class EmissionInformation extends DetailedChart{
  @CsvRecurse
  private Scope1 scope_1;

  @CsvRecurse
  private Scope2 scope_2;
  
  @CsvRecurse
  private Scope3 scope_3;

  public EmissionInformation() {
    super("GHG");
  }
}
