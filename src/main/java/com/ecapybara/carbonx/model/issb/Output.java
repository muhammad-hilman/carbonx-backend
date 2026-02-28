package com.ecapybara.carbonx.model.issb;

import org.springframework.data.annotation.PersistenceCreator;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.To;
import com.ecapybara.carbonx.utils.csv.IdToProcessConverter;
import com.ecapybara.carbonx.utils.csv.IdToProductConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Edge("outputs")
@PersistentIndex(fields = {"arangoId","id","processName","productName"})
public class Output extends com.ecapybara.carbonx.model.basic.Edge {

  @Setter(AccessLevel.NONE)
  @CsvBindByName
  private String processName;
  
  @Setter(AccessLevel.NONE)
  @CsvBindByName
  private String productName;

  @PersistenceCreator
  public Output(Process process, Product product) {
    super();
    this.setFrom(process.getId());
    this.processName = process.getName();
    this.setTo(product.getId());
    this.productName = product.getName();
  }

  // custom setters
  public void setProcess(Process process) {
    this.setFrom(process.getId());
    this.processName = process.getName();
  }
  public void setProduct(Product product) { 
    this.setTo(product.getId());
    this.productName = product.getName();
  }

  @Override
  public String toString() {
    try {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    } catch (Exception e) {
        return super.toString(); // fallback
    }
  }
}
