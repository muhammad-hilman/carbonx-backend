package com.ecapybara.carbonx.model.issb;

import org.springframework.data.annotation.PersistenceCreator;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.To;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.opencsv.bean.CsvBindByName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true) @SuperBuilder(toBuilder = true)
@Edge("inputs")
@PersistentIndex(fields = {"id","key","productName","processName"})
public class Input extends com.ecapybara.carbonx.model.basic.Edge {  
  @NonNull
  @From
  @JsonAlias({"_from"})
  @CsvBindByName(column = "_from")
  private Product product;
  
  @Setter(AccessLevel.NONE)
  private String productName;

  @NonNull
  @To
  @JsonAlias({"_to"})
  @CsvBindByName(column = "_to")
  private Process process;
  
  @Setter(AccessLevel.NONE)
  private String processName;

  @PersistenceCreator
  public Input(Product product, Process process) {
    super();
    this.product = product;
    this.productName = product.getName();
    this.process = process;
    this.processName = process.getName();
  }

  //custom setters
  public void setProduct(Product product) { 
    this.product = product;
    this.productName = product.getName();
  }
  public void setProcess(Process process) {
    this.process = process;
    this.processName = process.getName();
  }
}
