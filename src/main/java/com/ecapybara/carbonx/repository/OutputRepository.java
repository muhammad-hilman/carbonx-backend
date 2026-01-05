package com.ecapybara.carbonx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.arangodb.springframework.repository.ArangoRepository;

import com.ecapybara.carbonx.model.Output;

public interface OutputRepository extends ArangoRepository<Output, String> {

  List<Output> findByProcessName(String processName);

  List<Output> findByProductName(String productName);

  List<Output> findByProcessNameAndProductName(String fromProcessName, String toProductName);

  @NonNull Optional<Output> findById(@NonNull String id);
}
