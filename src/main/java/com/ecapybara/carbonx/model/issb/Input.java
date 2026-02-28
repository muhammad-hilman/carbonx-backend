package com.ecapybara.carbonx.model.issb;

import org.springframework.data.annotation.PersistenceCreator;

import com.arangodb.springframework.annotation.Edge;

import com.arangodb.springframework.annotation.PersistentIndex;
import com.ecapybara.carbonx.utils.csv.IdToProcessConverter;
import com.ecapybara.carbonx.utils.csv.IdToProductConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

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
  
  
  @Setter(AccessLevel.NONE)
  @CsvBindByName
  private String productName;
  
  @Setter(AccessLevel.NONE)
  @CsvBindByName
  private String processName;

  @PersistenceCreator
  public Input(Product product, Process process) {
    super();
    this.setFrom(product.getId());
    this.productName = product.getName();
    this.setTo(process.getId());
    this.processName = process.getName();
  }

  //custom setters
  public void setProduct(Product product) { 
    this.setFrom(product.getId());
    this.productName = product.getName();
  }
  public void setProcess(Process process) {
    this.setTo(process.getId());
    this.processName = process.getName();
  }

  @Override
  public String toString() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(this);
    } catch (Exception e) {
      return super.toString();
    }
  }
}
