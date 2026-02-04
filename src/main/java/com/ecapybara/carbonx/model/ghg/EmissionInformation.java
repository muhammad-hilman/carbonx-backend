package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.DetailedChart;
import com.opencsv.bean.CsvRecurse;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
public class EmissionInformation extends DetailedChart{
  @Builder.Default
  @CsvRecurse
  private Scope1 scope1 = new Scope1();

  @Builder.Default
  @CsvRecurse
  private Scope2 scope2 = new Scope2();

  @Builder.Default  
  @CsvRecurse
  private Scope3 scope3 = new Scope3();

  public EmissionInformation() {
    super("GHG");
  }
}
