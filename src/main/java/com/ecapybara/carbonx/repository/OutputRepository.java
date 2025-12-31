package com.ecapybara.carbonx.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ecapybara.carbonx.model.Output;

public interface OutputRepository extends ArangoRepository<Output, String> {

}
