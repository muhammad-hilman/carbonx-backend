package com.ecapybara.carbonx.model;


import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge("outputs")
public class Output {
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
      return "Produces [process=" + process + ", product=" + product + "]";
  }

  // setter & getter
  public Process getProcess() { return process; }
  public void setProcess(Process process) { this.process = process; }
  public Product getProduct() { return product; }
  public void setProduct(Product product) { this.product = product; }  
}
