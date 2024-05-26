package com.swp391.JewelryProduction.dto;

import com.swp391.JewelryProduction.pojos.ProductParameters;
import com.swp391.JewelryProduction.pojos.ProductSpecification;
import com.swp391.JewelryProduction.pojos.ValueDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParameterValueDTO {
    private Integer id;
    private String value;
    private String description;
    private ProductParameters parameter;
    private ValueDetails details;
    private ProductSpecification specification;
}
