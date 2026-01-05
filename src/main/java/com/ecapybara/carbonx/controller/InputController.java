package com.ecapybara.carbonx.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.model.Input;
import com.ecapybara.carbonx.repository.InputRepository;

@RestController
@RequestMapping("/api/inputs")
public class InputController {

  @Autowired
  private InputRepository inputRepository;

  @GetMapping
  public Iterable<Input> getInputs(@RequestParam(name = "from", required = false) String productName, @RequestParam(name = "to", required = false) String processName) {
    if (productName!=null && processName!=null && !productName.isEmpty() && !processName.isEmpty()) {
      return inputRepository.findByProductNameAndProcessName(productName, processName);
    }

    else if (productName != null && !productName.isEmpty()) {
      return inputRepository.findByProductName(productName);
    }

    else if (processName != null && !processName.isEmpty()) {
      return inputRepository.findByProcessName(processName);
    }
    
    else {
      return inputRepository.findAll();
    }
  }

  @GetMapping("/{id}")
  public Optional<Input> getInput(@PathVariable String id) {
    return inputRepository.findById(id);
  }
}
