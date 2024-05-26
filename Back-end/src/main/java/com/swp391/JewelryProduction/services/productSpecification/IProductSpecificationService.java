package com.swp391.JewelryProduction.services.productSpecification;

import com.swp391.JewelryProduction.dto.ProductSpecificationDTO;

import java.util.List;

public interface IProductSpecificationService {
    List<ProductSpecificationDTO> findAllProductSpecifications();
}
