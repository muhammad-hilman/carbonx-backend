package com.ecapybara.carbonx.model.basic;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter @SuperBuilder(toBuilder = true)
public class DetailedChart {
  @NonNull
  @CsvBindByName
  private final String description;

  public DetailedChart(String description) {
    this.description = description;
  }
}
