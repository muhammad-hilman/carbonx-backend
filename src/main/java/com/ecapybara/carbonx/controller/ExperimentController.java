package com.ecapybara.carbonx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.service.ExperimentalService;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {

  @Autowired
  ExperimentalService experimentalService;

  @PostMapping("/import")
  public Mono<?> importComplexCSV() {
      //TODO: process POST request
      
    return Mono.just("Something");
  }
  
}
