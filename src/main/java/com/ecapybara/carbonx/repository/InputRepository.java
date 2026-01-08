package com.ecapybara.carbonx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import com.arangodb.springframework.repository.ArangoRepository;

import com.ecapybara.carbonx.model.Input;

public interface InputRepository extends ArangoRepository<Input, String> {

  @NonNull Optional<Input> findById(@NonNull String id);

  List<Input> findByProductName(Sort sort, String productName);

  List<Input> findByProcessName(Sort sort, String processName);

  List<Input> findByProductNameAndProcessName(Sort sort, String fromProductName, String toProcessName);
}
