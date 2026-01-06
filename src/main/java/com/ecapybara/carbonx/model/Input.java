package com.ecapybara.carbonx.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.To;

@Edge("inputs")
@PersistentIndex(fields = {"id","productName","processName"})
public class Input {
  @ArangoId // db document field: _id
  private String arangoId;

  @Id // db document field: _key
  private String id;
  
  @From
  private Product product;
  private String productName;

  @To
  private Process process;
  private String processName;

  public Input() {
    super();
  }

  @PersistenceCreator
  public Input(final Product product, final Process process) {
    super();
    this.product = product;
    this.productName = product.getName();
    this.process = process;
    this.processName = process.getName();
  }
  
  @Override
  public String toString() {
    return "Input [id=" + id + ", product=" + productName + ", process=" + processName + "]";
  }

  // setter and getter
  public String getArangoId() { return arangoId; }
  public void setArangoId(String arangoId) { this.arangoId = arangoId; }
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public Product getProduct() { return product; }
  public String getProductName() { return productName; }
  public void setProduct(Product product) { 
    this.product = product;
    this.productName = product.getName();
  }
  public Process getProcess() { return process; }
  public String getProcessName() { return processName; }
  public void setProcess(Process process) { 
    this.process = process;
    this.processName = process.getName();
  }
}
