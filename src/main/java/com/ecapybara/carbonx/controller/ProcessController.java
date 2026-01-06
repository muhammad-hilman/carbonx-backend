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

import com.ecapybara.carbonx.model.Process;
import com.ecapybara.carbonx.repository.ProcessRepository;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

  @Autowired
  private ProcessRepository processRepository;

  final Sort sort = Sort.by(Direction.DESC, "id");

  @GetMapping
  public Iterable<Process> getProcesses(@RequestParam(name = "processName", required = false) String processName) {
    if (processName != null && !processName.isEmpty()) {
        return processRepository.findByName(sort, processName);
    }
    return processRepository.findAll();
  }

  @GetMapping("/{id}")
  public Optional<Process> getProcess(@PathVariable String id) {
    return processRepository.findById(id);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public Process createProcess(@RequestBody Process process) {
    System.out.println("New process created:");
    System.out.println(process.toString());

    processRepository.save(process);
    process = processRepository.findByName(sort, process.getName()).get(0);
    System.out.println("Created process saved into process database:");
    System.out.println(process.toString());
    
    return process;
  }
}