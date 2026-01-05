package com.ecapybara.carbonx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.arangodb.springframework.repository.ArangoRepository;

import com.ecapybara.carbonx.model.Input;

public interface InputRepository extends ArangoRepository<Input, String> {

  List<Input> findByProductName(String productName);

  List<Input> findByProcessName(String processName);

  List<Input> findByProductNameAndProcessName(String fromProductName, String toProcessName);

  @NonNull Optional<Input> findById(@NonNull String id);
}
