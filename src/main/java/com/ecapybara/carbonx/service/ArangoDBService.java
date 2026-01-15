package com.ecapybara.carbonx.service;

import org.springframework.stereotype.Service;

@Service
public class ArangoDBService {
  private final String ARANGODB_API_URL = "http://localhost:8529/_db/testCompany";
}
