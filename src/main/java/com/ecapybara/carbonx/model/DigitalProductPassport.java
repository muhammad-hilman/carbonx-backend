package com.ecapybara.carbonx.model;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.fasterxml.jackson.annotation.JsonAlias;

@Document("DPPs")
@PersistentIndex(fields = {"name"})
public class DigitalProductPassport {

  @ArangoId // db document field: _id
  @JsonAlias({"_id"})
  private String id;

  @Id // db document field: _key
  @JsonAlias({"_key"})
  private String key;

  private String serialNumber;
  private String manufacturer;
  private String process;
  private Double weight;

  //constructor
  public DigitalProductPassport() {
    super();
  }

  public DigitalProductPassport(String component, String process, double weight) {
    super();
    this.process = process;
    this.weight = weight;
  }

  // getters & setters
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getKey() { return key; }
  public void setKey(String key) { this.key = key; }  
  public String getProcess() { return process; }
  public void setProcess(String process) { this.process = process; }
  public double getWeight() { return weight; }
  public void setWeight(double weight) { this.weight = weight; }
  
  // Alias for weight (used in some controllers)
  public double getWeightKg() {
    return weight;
  }
  public void setWeightKg(double weight) {
    this.weight = weight;
  }
}
