package com.ecapybara.carbonx.model.issb;

import org.springframework.data.annotation.PersistenceCreator;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.To;
import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
@Edge("outputs")
@PersistentIndex(fields = {"arangoId","id","processName","productName"})
public class Output extends com.ecapybara.carbonx.model.basic.Edge{

  @NonNull
  @From
  @JsonAlias({"_from"})
  private Process process;

  @Setter(AccessLevel.NONE)
  private String processName;

  @NonNull
  @To
  @JsonAlias({"_to"})
  private Product product;
  
  @Setter(AccessLevel.NONE)
  private String productName;

  @PersistenceCreator
  public Output(Process process, Product product) {
    super();
    this.process = process;
    this.processName = process.getName();
    this.product = product;
    this.productName = product.getName();
  }

  // custom setters
  public void setProcess(Process process) {
    this.process = process;
    this.processName = process.getName();
  }
  public void setProduct(Product product) { 
    this.product = product;
    this.productName = product.getName();
  }
}
