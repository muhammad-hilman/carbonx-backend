package com.ecapybara.carbonx.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import com.arangodb.springframework.repository.ArangoRepository;

import com.ecapybara.carbonx.model.Process;

public interface ProcessRepository extends ArangoRepository<Process, String>{
  
  List<Process> findByName(String name);

  @NonNull Optional<Process> findById(@NonNull String id);

  Collection<Process> findByNameContainingIgnoreCase(String name); //check whether ArangoDB supports this search format criteria by default
}