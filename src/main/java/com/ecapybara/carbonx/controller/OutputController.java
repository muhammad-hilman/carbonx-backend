package com.ecapybara.carbonx.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.model.Output;
import com.ecapybara.carbonx.repository.OutputRepository;

@RestController
@RequestMapping("/api/outputs")
public class OutputController {

  @Autowired
  private OutputRepository outputRepository;

  @GetMapping
  public Iterable<Output> getOutputs(@RequestParam(name = "from", required = false) String processName, @RequestParam(name = "to", required = false) String productName) {
    if (productName != null && processName != null && !productName.isEmpty() && !processName.isEmpty()) {
      return outputRepository.findByProcessNameAndProductName(processName, productName);
    }

    else if (productName != null && !productName.isEmpty()) {
      return outputRepository.findByProductName(productName);
    }

    else if (processName != null && !processName.isEmpty()) {
      return outputRepository.findByProcessName(processName);
    }
    
    else {
      return outputRepository.findAll();
    }
  }

  @GetMapping("/{id}")
  public Optional<Output> getOutput(@PathVariable String id) {
    return outputRepository.findById(id);
  }
}
