package com.ecapybara.carbonx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.repository.UserRepository;

@RestController
@RequestMapping("/api/company/user")
public class UserController {
  @Autowired
  private UserRepository userRepository;

}
