package com.swp391.JewelryProduction.services.valueDetails;

import com.swp391.JewelryProduction.dto.ValueDetailsDTO;

import java.util.List;

public interface IValueDetailsService {
    List<ValueDetailsDTO> findAllValueDetails();
}
