package com.swp391.JewelryProduction.services.product;

import com.swp391.JewelryProduction.dto.ProductDTO;
import com.swp391.JewelryProduction.pojos.Product;
import com.swp391.JewelryProduction.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService{
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> findAllProducts() {
//        return productRepository.findAll().stream().map(product -> mapToProductDTO(product)).collect(Collectors.toList());
        return null;
    }
}
