package com.swp391.JewelryProduction.services.product;

import com.swp391.JewelryProduction.pojos.designPojos.Product;
import com.swp391.JewelryProduction.pojos.designPojos.ProductSpecification;
import com.swp391.JewelryProduction.repositories.ProductRepository;
import com.swp391.JewelryProduction.repositories.ProductSpecificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductSpecificationRepository productSpecificationRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().toList();
    }

    @Override
    public ProductSpecification findProductSpecificationById(int id) {
        if(productSpecificationRepository.findById(id).isPresent()) {
            return productSpecificationRepository.findById(id).get();
        } else return null;
    }

    @Override
    public Product saveProduct(Product product) {
        productRepository.save(product);
        return product;
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductSpecification save(ProductSpecification specs) {
        for(ProductSpecification proSpecs : productSpecificationRepository.findAll()) {
            if(proSpecs.getGemstone().equals(specs.getGemstone()) &&
                    proSpecs.getChainType().equals(specs.getChainType()) &&
                    proSpecs.getLength().equals(specs.getLength()) &&
                    proSpecs.getStyle().equals(specs.getStyle()) &&
                    proSpecs.getMetal().equals(specs.getMetal()) &&
                    proSpecs.getShape().equals(specs.getShape()) &&
                    proSpecs.getOccasion().equals(specs.getOccasion()) &&
                    proSpecs.getTexture().equals(specs.getTexture()) &&
                    proSpecs.getType().equals(specs.getType()) &&
                    proSpecs.getGemstoneWeight().equals(specs.getGemstoneWeight())) return proSpecs;
            else productSpecificationRepository.save(specs);
        }
        return specs;
    }
}
