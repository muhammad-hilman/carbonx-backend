package com.ecapybara.carbonx.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.model.Input;
import com.ecapybara.carbonx.repository.InputRepository;

@RestController
@RequestMapping("/api/inputs")
public class InputController {

  @Autowired
  private InputRepository inputRepository;
  final Sort sort = Sort.by(Direction.DESC, "id");

  @GetMapping
  public Iterable<Input> getInputs(@RequestParam(name = "from", required = false) String productName, @RequestParam(name = "to", required = false) String processName) {
    if (productName!=null && processName!=null && !productName.isEmpty() && !processName.isEmpty()) {
      return inputRepository.findByProductNameAndProcessName(sort, productName, processName);
    }

    else if (productName != null && !productName.isEmpty()) {
      return inputRepository.findByProductName(sort, productName);
    }

    else if (processName != null && !processName.isEmpty()) {
      return inputRepository.findByProcessName(sort, processName);
    }
    
    else {
      return inputRepository.findAll();
    }
  }

  @GetMapping("/{id}")
  public Optional<Input> getInput(@PathVariable String id) {
    return inputRepository.findById(id);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public Input createInput(@RequestBody Input input) {
    System.out.println("New input created:");
    System.out.println(input.toString());

    inputRepository.save(input);
    input = inputRepository.findByProductNameAndProcessName(sort, input.getProductName(), input.getProcessName()).get(0);
    System.out.println("Created input saved into input database:");
    System.out.println(input.toString());
    
    return input;
  }
}
