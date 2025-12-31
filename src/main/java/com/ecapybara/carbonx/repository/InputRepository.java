package com.ecapybara.carbonx.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ecapybara.carbonx.model.Input;

public interface InputRepository extends ArangoRepository<Input, String> {

}
