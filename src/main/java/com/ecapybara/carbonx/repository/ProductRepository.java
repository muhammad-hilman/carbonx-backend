package com.ecapybara.carbonx.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import com.arangodb.springframework.repository.ArangoRepository;

import com.ecapybara.carbonx.model.Product;

public interface ProductRepository extends ArangoRepository<Product, String>{
  List<Product> findByName(Sort sort, String name);

  @NonNull Optional<Product> findById(@NonNull String id);

  Collection<Product> findByNameContainingIgnoreCase(String name); //check whether ArangoDB supports this search format criteria by default
}