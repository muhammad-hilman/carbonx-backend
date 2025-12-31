package com.ecapybara.carbonx.model;

import java.util.Collection;
import java.util.Properties;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;

@Document("processes")
@PersistentIndex(fields = {"name"})
public class Process {
  @Id // db document field: _key
  private String id;

  @ArangoId // db document field: _id
  private String arangoId;

  private String name;
  private String processType;
  private Properties functionalProperties;
  private Properties nonFunctionalProperties;

  @Relations(edges = Input.class, lazy=true)
  private Collection<Product> inputs;

  // constructors
  public Process(String name) {
    this.name = name;
  }

  public Process(String processType, String name) {
    this.name = name;
    this.processType = processType;
  }

  // setters and getters
  public String getId() {return id;}
  public void setId(String id) {this.id = id;}
  public String getArangoId() {return arangoId;}
  public void setArangoId(String arangoId) {this.arangoId = arangoId;}
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}
  public String getProcessType() {return processType;}
  public void setProcessType(String processType) {this.processType = processType;}
  public Properties getFunctionalProperties() {return functionalProperties;}
  public void setFunctionalProperties(Properties functionalProperties) {this.functionalProperties = functionalProperties;}
  public Properties getNonFunctionalProperties() {return nonFunctionalProperties;}
  public void setNonFunctionalProperties(Properties nonFunctionalProperties) {this.nonFunctionalProperties = nonFunctionalProperties;}
  public Collection<Product> getInputs() { return inputs; }

  @Override
  public String toString() {
    return "Process [id=" + id + ", name=" + name + ", processType= " + processType + "]";
  }
}
