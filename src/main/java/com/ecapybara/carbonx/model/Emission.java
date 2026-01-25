package com.ecapybara.carbonx.model;

public class Emission {
  private String name; // eg. "CO"
  private Double value; // eg. 22.5
  private String unit; // eg. "cm3"

  // constructors
  public Emission() {
    super();
  }
  public Emission(String name, Double value, String unit) {
    super();
    this.name = name;
    this.value = value;
    this.unit = unit;
  }

  // getters and setters
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Double getValue() {
    return value;
  }
  public void setValue(Double value) {
    this.value = value;
  }
  public String getUnit() {
    return unit;
  }
  public void setUnit(String unit) {
    this.unit = unit;
  }   
}
