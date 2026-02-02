package com.ecapybara.carbonx.model.basic;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter @SuperBuilder(toBuilder = true)
public class DetailedChart {
  @NonNull
  private final String description;

  public DetailedChart(String description) {
    this.description = description;
  }
}
