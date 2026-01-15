package com.ecapybara.carbonx.model;

import java.util.Collection;
import java.util.Properties;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;


import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;

@Document("products")
@PersistentIndex(fields = {"arangoId", "id","name","productNature","productOrigin","userId"})
public class Product {
  
  @ArangoId // db document field: _id
  private String arangoId;

  @Id // db document field: _key
  private String id;

  private String name; // e.g Tesla
  private String productNature; // e.g Car
  private String productOrigin; // e.g supplier/user
  private Properties functionalProperties;
  private DigitalProductPassport DPP;

  @Relations(edges = Output.class, lazy=true)
  private Collection<Process> procedure;
  /* 
  @Relations(edges = Input.class, lazy=true)
  private Collection<Process> usedIn;
  */
  
  // Additional fields for inventory management
  private String userId; // User who owns this product
  private String uploadedFile; // Filename of uploaded BOM file

  // constructors
  public Product() {
    super();
  }

  public Product(final String productNature) {
    super();
    this.productNature = productNature;
  }
  
  @PersistenceCreator
  public Product(final String productNature, final String name) {
    super();
    this.name = name;
    this.productNature = productNature;
  }

  public Product(final String productNature, final String name, final Properties functionalProperties, DigitalProductPassport DPP) {
    super();
    this.name = name;
    this.productNature = productNature;
    this.functionalProperties = functionalProperties;
    this.DPP = DPP;
  }

  // getters & setters
  public String getArangoId() {return arangoId;}
  public void setArangoId(String arangoId) {this.arangoId = arangoId;}
  public String getId() {return id;  }
  public void setId(String id) {this.id = id;}
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}
  public String getProductNature() {return productNature;}
  public void setProductNature(String productNature) {this.productNature = productNature;}
  public String getProductOrigin() {return productOrigin;}
  public void setProductOrigin(String productOrigin) {this.productOrigin = productOrigin;}
  public Properties getFunctionalProperties() {return functionalProperties;}
  public void setFunctionalProperties(Properties functionalProperties) {this.functionalProperties = functionalProperties;}
  public DigitalProductPassport getDPP() {return DPP;}
  public void setDPP(DigitalProductPassport DPP) {this.DPP = DPP;}
  public Collection<Process> getProcedure() {return procedure;}
  /*public Collection<Process> getUsedIn() {return usedIn;}*/
  
  // Getters and setters for inventory fields
  public String getUserId() {return userId;}
  public void setUserId(String userId) {this.userId = userId;}  
  public String getUploadedFile() {return uploadedFile;}
  public void setUploadedFile(String uploadedFile) {this.uploadedFile = uploadedFile;}

  @Override
  public String toString() {
    return "Product [id=" + id + ", name=" + name + ", productNature=" + productNature + ", productOrigin=" + productOrigin + "]";
  }
}
