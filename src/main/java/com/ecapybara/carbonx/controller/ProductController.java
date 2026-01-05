package com.ecapybara.carbonx.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.model.Product;
import com.ecapybara.carbonx.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private ProductRepository productRepository;

  @GetMapping
  public Iterable<Product> getProducts(@RequestParam(name = "productName", required = false) String productName) {
    if (productName != null && !productName.isEmpty()) {
      return productRepository.findByName(productName);
    }
    return productRepository.findAll();
  }

  @GetMapping("/{id}")
  public Optional<Product> getProduct(@PathVariable String id) {
    return productRepository.findById(id);
  }
}
