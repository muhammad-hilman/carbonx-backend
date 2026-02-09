package com.ecapybara.carbonx.model.basic;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.ecapybara.carbonx.model.ghg.CarbonFootprint;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
@Document("DPPs")
@PersistentIndex(fields = {"name", "manufacturer", "serialNumber"})
public class DigitalProductPassport {

  @ArangoId // db document field: _id
  @JsonAlias({"_id"})
  private String id;

  @Id // db document field: _key
  @JsonAlias({"_key"})
  private String key;

  @NonNull
  @CsvBindByName(column = "dpp.name")
  private String name; // e.g Model X
  
  @NonNull
  @CsvBindByName
  private String manufacturer; // e.g Tesla

  @CsvBindByName
  private String serialNumber;

  @CsvBindByName
  private String batchNumber;

  @Builder.Default
  @CsvRecurse
  private CarbonFootprint carbonFootprint = new CarbonFootprint();
}
