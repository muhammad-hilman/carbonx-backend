package com.ecapybara.carbonx.utils.csv;

import com.opencsv.bean.AbstractBeanField;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ComplexMapConverter extends AbstractBeanField<Map<String, Map<String, Double>>, String> {

  @Override
  protected Map<String, Map<String, Double>> convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1); // Remove outer {}
        }

        Map<String, Map<String, Double>> result = new HashMap<>();
        String[] outerPairs = trimmed.contains(";") ? trimmed.split(";") : new String[]{trimmed};
        
        for (String outerPair : outerPairs) {
            if (outerPair.trim().isEmpty()) continue;
            String[] outerKV = outerPair.split(":");
            if (outerKV.length != 2) continue;
            
            String gasName = outerKV[0].trim();  // "CO2", "CH4"
            String innerStr = outerKV[1].trim();
            
            // Single key-value pair: {kg:1.0} -> "kg:1.0"
            if (innerStr.startsWith("{") && innerStr.endsWith("}")) {
                innerStr = innerStr.substring(1, innerStr.length() - 1);
            }
            
            // Expect exactly one pair: unit:value
            String[] unitValue = innerStr.split(":");
            if (unitValue.length == 2) {
                String unit = unitValue[0].trim();  // "kg"
                Double amount = Double.parseDouble(unitValue[1].trim());  // 1.0
                
                Map<String, Double> innerMap = new HashMap<>();
                innerMap.put(unit, amount);
                result.put(gasName, innerMap);
            }
        }
        
        return result;
    }
}