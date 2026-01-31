package com.ecapybara.carbonx.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExperimentalService {
  
  @Autowired
  private WebClient webClient;

  public Mono<?> importComplexCSV() {
    return Mono.just("something");
  }
}
