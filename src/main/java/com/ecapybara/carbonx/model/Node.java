package com.ecapybara.carbonx.model;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.ArangoId;
import com.ecapybara.carbonx.model.emissions.Emission;
import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @SuperBuilder(toBuilder = true) 
public class Node {
  @ArangoId // db document field: _id
  @JsonAlias({"_id"})
  private String id;

  @Id // db document field: _key
  @JsonAlias({"_key"})
  private String key;

  private String name;
  private String type;
  private String quantifiableUnit;
  private Double quantityValue;
  private Map<String,Map<String,List<Emission>>> emissionInformation; // e.g {"Scope 1" : ExtractionEmissionCharts, "Scope 2" : ProcessingEmissionCharts, "Scope 3" : TransportationEmissionCharts}

  private Properties functionalProperties;
  private DigitalProductPassport DPP;
}
