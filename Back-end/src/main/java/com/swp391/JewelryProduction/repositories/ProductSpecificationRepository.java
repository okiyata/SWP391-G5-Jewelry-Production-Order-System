package com.swp391.JewelryProduction.repositories;

import com.swp391.JewelryProduction.pojos.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, UUID> {
}
