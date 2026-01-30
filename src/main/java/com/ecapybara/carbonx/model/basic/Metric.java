package com.ecapybara.carbonx.model.basic;

public class Metric {
  private Double value;
  private String unit;

  // constructors
  public Metric() {
    super();
  }

  public Metric(Double value, String unit) {
    super();
    this.unit = unit;
    this.value = value;
  }

  // setters and getters
  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }
}
