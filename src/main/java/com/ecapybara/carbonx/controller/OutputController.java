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

import com.ecapybara.carbonx.model.Output;
import com.ecapybara.carbonx.repository.OutputRepository;

@RestController
@RequestMapping("/api/outputs")
public class OutputController {

  @Autowired
  private OutputRepository outputRepository;
  final Sort sort = Sort.by(Direction.DESC, "id");

  @GetMapping
  public Iterable<Output> getOutputs(@RequestParam(name = "from", required = false) String processName, @RequestParam(name = "to", required = false) String productName) {
    if (productName != null && processName != null && !productName.isEmpty() && !processName.isEmpty()) {
      return outputRepository.findByProcessNameAndProductName(sort, processName, productName);
    }

    else if (productName != null && !productName.isEmpty()) {
      return outputRepository.findByProductName(sort, productName);
    }

    else if (processName != null && !processName.isEmpty()) {
      return outputRepository.findByProcessName(sort, processName);
    }
    
    else {
      return outputRepository.findAll();
    }
  }

  @GetMapping("/{id}")
  public Optional<Output> getOutput(@PathVariable String id) {
    return outputRepository.findById(id);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public Output createOutput(@RequestBody Output output) {
    System.out.println("New output created:");
    System.out.println(output.toString());

    outputRepository.save(output);
    output = outputRepository.findByProcessNameAndProductName(sort, output.getProcessName(), output.getProductName()).get(0);
    System.out.println("Created input saved into input database:");
    System.out.println(output.toString());
    
    return output;
  }
}
