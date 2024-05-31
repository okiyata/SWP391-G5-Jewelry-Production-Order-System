package com.swp391.JewelryProduction.repositories;

import com.swp391.JewelryProduction.dto.MaterialDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<MaterialDTO, Long> {

}
