package com.swp391.JewelryProduction.services.product;

import com.swp391.JewelryProduction.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<ProductDTO> findAllProducts();
}
