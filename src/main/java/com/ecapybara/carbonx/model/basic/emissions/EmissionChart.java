package com.ecapybara.carbonx.model.basic.emissions;

import java.util.List;

public class EmissionChart {
  // private String chartType; // eg. "Scope 3, Category 1"
  // private String ProductOrProcessName; // e.g "Toyota Forerunner"
  // private String variant; //e.g "diesel fuel"
  private List<Emission> emissions; 

  // constructors
  public EmissionChart() {
    super();
  }
  public EmissionChart(List<Emission> emissions) {
    super();
    this.emissions = emissions;
  }
  /*
  public EmissionChart(String chartType, String productOrProcessName, String variant, Collection<Emission> emissions) {
    super();
    this.chartType = chartType;
    this.ProductOrProcessName = productOrProcessName;
    this.variant = variant;
    this.emissions = emissions;
  }
  */

  // getters and setters
  /*
  public String getChartType() {
    return chartType;
  }
  public void setChartType(String chartType) {
    this.chartType = chartType;
  }
  public String getProductOrProcessName() {
    return ProductOrProcessName;
  }
  public void setProductOrProcessName(String productOrProcessName) {
    ProductOrProcessName = productOrProcessName;
  }
  public String getVariant() {
    return variant;
  }
  public void setVariant(String variant) {
    this.variant = variant;
  }
  */
  public List<Emission> getEmissions() {
    return emissions;
  }
  public void setEmissions(List<Emission> emissions) {
    this.emissions = emissions;
  }
}
