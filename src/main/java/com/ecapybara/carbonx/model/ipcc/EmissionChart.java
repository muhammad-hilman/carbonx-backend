package com.ecapybara.carbonx.model.ipcc;

import com.ecapybara.carbonx.model.basic.Metric;

import lombok.Data;

@Data
public class EmissionChart {
  private Metric CO2;
  private Metric CH4;
  private Metric N2O;
  private Metric SF6;
  private HydrofluorocarbonChart HFCs;
  private PerfluorocarbonChart PFCs;
  private Metric NF3;
  private Metric H20;
  private Metric O3;
  private ChlorofluorocarbonChart CFCs;
}
