package com.ecapybara.carbonx.model;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge("inputs")
public class Input {
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
      return "UsedIn [product=" + product + ", process=" + process + "]";
  }

  // setter and getter
  public Product getProduct() { return product; }
  public void setProduct(Product product) { this.product = product; }
  public Process getProcess() { return process; }
  public void setProcess(Process process) { this.process = process; }
}
