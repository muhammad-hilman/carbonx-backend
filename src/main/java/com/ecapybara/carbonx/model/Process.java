package com.ecapybara.carbonx.model;

import java.util.Collection;

import org.springframework.data.annotation.PersistenceCreator;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;

@Document("processes")
@PersistentIndex(fields = {"id","key","name", "processType"})
public class Process extends Node {

  @Relations(edges = Input.class, lazy = true)
  private Collection<Product> inputs;

  // constructors
  public Process() {
    super();
  }

  public Process(String name) {
    super();
    this.setName(name);
  }

  @PersistenceCreator
  public Process(String processType, String name) {
    super(processType, name);
  }

  // setters and getters
  public Collection<Product> getInputs() { return inputs; }

  @Override
  public String toString() {
    return "Process [id= " + this.getId() + ", name= " + this.getName() + ", processType= " + this.getType() + "]";
  }
}
