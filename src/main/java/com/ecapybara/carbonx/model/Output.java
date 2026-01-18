package com.ecapybara.carbonx.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.To;
import com.fasterxml.jackson.annotation.JsonAlias;

@Edge("outputs")
@PersistentIndex(fields = {"arangoId","id","processName","productName"})
public class Output {
  @ArangoId // db document field: _id
  @JsonAlias({"_id"})
  private String id;

  @Id // db document field: _key
  @JsonAlias({"_key"})
  private String key;

  @From
  @JsonAlias({"_from"})
  private Process process;
  private String processName;

  @To
  @JsonAlias({"_to"})
  private Product product;
  private String productName;

  public Output() {
    super();
  }

  @PersistenceCreator
  public Output(final Process process, final Product product) {
    super();
    this.product = product;
    this.productName = product.getName();
    this.process = process;
    this.processName = process.getName();
  }
  
  @Override
  public String toString() {
      return "Output [id=" + id + ", process=" + processName + ", product=" + productName + "]";
  }

  // setter & getter
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getKey() { return key; }
  public void setKey(String key) { this.key = key; }
  public Process getProcess() { return process; }
  public String getProcessName() { return processName; }
  public void setProcess(Process process) {
    this.process = process;
    this.processName = process.getName();
  }
  public Product getProduct() { return product; }
  public String getProductName() { return productName; }
  public void setProduct(Product product) { 
    this.product = product;
    this.productName = product.getName();
  }  
}
