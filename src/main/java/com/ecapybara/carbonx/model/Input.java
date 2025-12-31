package com.ecapybara.carbonx.model;

import com.arangodb.serde.jackson.From;
import com.arangodb.serde.jackson.Id;
import com.arangodb.serde.jackson.To;
import com.arangodb.springframework.annotation.Edge;

@Edge
public class Input {

  @Id
  private String id;

  @From
  private Product product;

  @To
  private Process process;

  public Input(final Product product, final Process process) {
    super();
    this.product = product;
    this.process = process;
  }
  
  @Override
  public String toString() {
      return "UsedIn [id=" + id + ", product=" + product + ", process=" + process + "]";
  }

  // setter and getter
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public Product getProduct() { return product; }
  public void setProduct(Product product) { this.product = product; }
  public Process getProcess() { return process; }
  public void setProcess(Process process) { this.process = process; }
}
