package com.ecapybara.carbonx.model.ipcc;

import com.ecapybara.carbonx.model.basic.Emission;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class HydrofluorocarbonChart{
  private Emission HFC_23;
  private Emission HFC_134a;
  private Emission HFC_152a;
  private Emission HFC_125;
  private Emission HFC_143a;
  private Emission HFC_32;
  private Emission HFC_227ea;
  private Emission HFC_245fa;
  private Emission HFC_365mfc;
  private Emission HFC_43_10mee;
}
