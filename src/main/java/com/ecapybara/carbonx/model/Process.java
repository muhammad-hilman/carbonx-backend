package com.ecapybara.carbonx.model;

import java.util.Collection;
import java.util.Properties;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;


import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;
import com.fasterxml.jackson.annotation.JsonAlias;

@Document("processes")
@PersistentIndex(fields = {"id","key","name", "processType"})
public class Process {
  @ArangoId // db document field: _id
  @JsonAlias({"_id"})
  private String id;

  @Id // db document field: _key
  @JsonAlias({"_key"})
  private String key;

  private String name;
  private String processType;
  private Properties functionalProperties;
  private Properties nonFunctionalProperties;

  @Relations(edges = Input.class, lazy = true)
  private Collection<Product> inputs;

  // constructors
  public Process() {
    super();
  }

  public Process(String name) {
    super();
    this.name = name;
  }

  @PersistenceCreator
  public Process(String processType, String name) {
    super();
    this.name = name;
    this.processType = processType;
  }

  public Process(String processType, String name, Properties functionalProperties, Properties nonFunctionalProperties) {
    super();
    this.name = name;
    this.processType = processType;
    this.functionalProperties = functionalProperties;
    this.nonFunctionalProperties = nonFunctionalProperties;
  }

  // setters and getters
  public String getId() {return id;}
  public void setId(String id) {this.id = id;}
  public String getKey() {return key;}
  public void setKey(String key) {this.key = key;}
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
