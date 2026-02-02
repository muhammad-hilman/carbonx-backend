package com.ecapybara.carbonx.model.ghg;

import com.ecapybara.carbonx.model.basic.Metric;

import com.opencsv.bean.CsvRecurse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class CarbonFootprint {
  @CsvRecurse
  private Metric Scope1;

  @CsvRecurse
  private Metric Scope2;
  
  @CsvRecurse
  private Metric Scope3; //eg. {"Category 1": metricObject, "Category 2": metricObject}
}
