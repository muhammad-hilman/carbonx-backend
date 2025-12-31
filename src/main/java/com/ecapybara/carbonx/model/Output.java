package com.ecapybara.carbonx.model;

import com.arangodb.serde.jackson.From;
import com.arangodb.serde.jackson.Id;
import com.arangodb.serde.jackson.To;
import com.arangodb.springframework.annotation.Edge;

@Edge
public class Output {

  @Id
  private String id;

  @From
  private Process process;

  @To
  private Product product;

  public Output(final Process process, final Product product) {
    super();
    this.product = product;
    this.process = process;
  }
  
  @Override
  public String toString() {
      return "Produces [id=" + id + ", process=" + process + ", product=" + product + "]";
  }

  // setter & getter
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public Process getProcess() { return process; }
  public void setProcess(Process process) { this.process = process; }
  public Product getProduct() { return product; }
  public void setProduct(Product product) { this.product = product; }  
}
