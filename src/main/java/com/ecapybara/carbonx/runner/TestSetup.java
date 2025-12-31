package com.ecapybara.carbonx.runner;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import com.arangodb.springframework.core.ArangoOperations;
import com.ecapybara.carbonx.model.Product;
import com.ecapybara.carbonx.model.Process;
import com.ecapybara.carbonx.model.Input;
import com.ecapybara.carbonx.model.Output;
import com.ecapybara.carbonx.repository.InputRepository;
import com.ecapybara.carbonx.repository.OutputRepository;
import com.ecapybara.carbonx.repository.ProcessRepository;
import com.ecapybara.carbonx.repository.ProductRepository;

@ComponentScan("com.ecapybara.carbonx")
public class TestSetup implements CommandLineRunner {
  @Autowired
  private ArangoOperations operations;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ProcessRepository processRepository;
  @Autowired
  private InputRepository inputRepository;
  @Autowired
  private OutputRepository outputRepository;
  
  @Override
  public void run(final String... args) throws Exception {
    System.out.println("------------- # SETUP BEGIN # -------------");
    // first drop the database so that we can run this multiple times with the same dataset
    operations.dropDatabase();

    // Create and save products
    Product spaghetti = new Product("dish", "spaghetti");
    Product rawPasta = new Product("ingredient", "rawPasta");
    Product pasta = new Product("ingredient", "pasta");
    Product tomatoSauce = new Product("ingredient", "tomato sauce");
    Product hotDogs = new Product("ingredient", "hot dogs");
    Product salt = new Product("ingredient", "salt");
    Product pepper = new Product("ingredient", "pepper");
    Product sugar = new Product("ingredient", "sugar");
    Product garlic = new Product("ingredient", "garlic");
    Product onions = new Product("ingredient", "onions");
    Product cheese = new Product("ingredient", "cheese");
    Product tomatoPaste = new Product("ingredient", "tomato paste");
    Product oliveOil = new Product("ingredient", "olive oil");
    Product cleanWater = new Product("ingredient", "clean water");
    Product wasteWater = new Product("waste", "waste water");
    
    productRepository.saveAll(Arrays.asList(
      spaghetti,
      rawPasta,
      pasta,
      tomatoSauce,
      hotDogs,
      salt,
      pepper,
      sugar,
      garlic,
      onions,
      cheese,
      tomatoPaste,
      oliveOil,
      cleanWater
    ));

    long count = productRepository.count();
    System.out.println(String.format("%s PRODUCT entries created", count));

    // Create and save processes
    Process boiling = new Process("cooking", "boiling");
    Process simmering = new Process("cooking", "simmering");
    Process combining = new Process("cooking", "combining");
    
    
    processRepository.saveAll(Arrays.asList(
      boiling,
      simmering,
      combining
    ));

    count = processRepository.count();
    System.out.println(String.format("%s PROCESS entries created", count));

    // Create and save input relationships between entities
    inputRepository.saveAll(Arrays.asList(
      new Input(rawPasta, boiling),
      new Input(cleanWater, boiling),
      new Input(cleanWater, simmering),
      new Input(tomatoPaste, simmering),
      new Input(salt, simmering),
      new Input(sugar, simmering),
      new Input(pepper, simmering),
      new Input(garlic, simmering),
      new Input(onions, simmering),
      new Input(tomatoSauce, combining),
      new Input(pasta, combining),
      new Input(hotDogs, combining),
      new Input(cheese, combining),
      new Input(oliveOil, combining)
    ));

    outputRepository.saveAll(Arrays.asList(
      new Output(boiling, pasta),
      new Output(boiling, wasteWater),
      new Output(simmering, tomatoSauce),
      new Output(combining, spaghetti)
    ));

    System.out.println("------------- # SETUP COMPLETE # -------------");    
  }
}
