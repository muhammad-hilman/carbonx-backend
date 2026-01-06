package com.ecapybara.carbonx.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import com.arangodb.springframework.repository.ArangoRepository;

import com.ecapybara.carbonx.model.Output;

public interface OutputRepository extends ArangoRepository<Output, String> {

  List<Output> findByProcessName(Sort sort, String processName);

  List<Output> findByProductName(Sort sort, String productName);

  List<Output> findByProcessNameAndProductName(Sort sort, String fromProcessName, String toProductName);

  @NonNull Optional<Output> findById(@NonNull String id);
}
