package com.ecapybara.carbonx.model.basic;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.ArangoId;
import com.ecapybara.carbonx.model.ghg.EmissionInformation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @AllArgsConstructor @SuperBuilder(toBuilder = true)
public abstract class Node {

  @ArangoId // db document field: _id
  @JsonProperty("_id")
  @CsvBindByName
  private String id;

  @Id // db document field: _key
  @JsonProperty("_key")
  @Setter(AccessLevel.NONE) // No setter generated for this field
  private String key;

  @Builder.Default
  @CsvRecurse
  private EmissionInformation emissionInformation = new EmissionInformation(); // e.g {"Scope 1" : ExtractionEmissionCharts, "Scope 2" : ProcessingEmissionCharts, "Scope 3" : TransportationEmissionCharts}
  
  @Builder.Default
  @CsvRecurse
  private DigitalProductPassport DPP = new DigitalProductPassport();

  public void setId(String id) {
    this.id = id;
    this.key = id.split("/")[1];
  }
}
