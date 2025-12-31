package com.ecapybara.carbonx.repository;

import java.util.Collection;
import java.util.List;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ecapybara.carbonx.model.Product;

public interface ProductRepository extends ArangoRepository<Product, String>{
  
  List<Product> findByName(String name);

  Collection<Product> findByNameContainingIgnoreCase(String name); //check whether ArangoDB supports this search format criteria by default

  Collection<Product> findAll();
}