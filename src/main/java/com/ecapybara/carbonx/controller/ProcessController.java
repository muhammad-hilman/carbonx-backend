package com.ecapybara.carbonx.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.model.Process;
import com.ecapybara.carbonx.repository.ProcessRepository;

@RestController
@RequestMapping("/api/processes")
public class ProcessController {

  @Autowired
  private ProcessRepository processRepository;

  @GetMapping
  public Iterable<Process> getProcesses(@RequestParam(name = "processName", required = false) String processName) {
    if (processName != null && !processName.isEmpty()) {
        return processRepository.findByName(processName);
    }
    return processRepository.findAll();
  }

  @GetMapping("/{id}")
  public Optional<Process> getProcess(@PathVariable String id) {
    return processRepository.findById(id);
  }
}