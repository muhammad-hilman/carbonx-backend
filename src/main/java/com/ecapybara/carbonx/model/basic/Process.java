package com.ecapybara.carbonx.model.basic;

import java.util.Collection;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true) 
@Document("processes")
@PersistentIndex(fields = {"id","key","name", "type", "serviceProvider"})
public class Process extends Node {

  private String serviceProvider;

  @Relations(edges = Input.class, lazy = true)
  private Collection<Product> inputs;

  @Override
  public String toString() {
    return "Process [id= " + this.getId() + ", name= " + this.getName() + ", processType= " + this.getType() + "]";
  }
}
