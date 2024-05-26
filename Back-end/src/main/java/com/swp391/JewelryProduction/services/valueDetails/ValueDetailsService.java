package com.swp391.JewelryProduction.services.valueDetails;

import com.swp391.JewelryProduction.dto.ValueDetailsDTO;
import com.swp391.JewelryProduction.pojos.ValueDetails;
import com.swp391.JewelryProduction.repositories.ValueDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValueDetailsService implements IValueDetailsService{
    private ValueDetailsRepository valueDetailsRepository;

    @Autowired
    public ValueDetailsService(ValueDetailsRepository valueDetailsRepository) {
        this.valueDetailsRepository = valueDetailsRepository;
    }

    @Override
    public List<ValueDetailsDTO> findAllValueDetails() {
        return valueDetailsRepository.findAll().stream().map(valueDetail -> mapToValueDetailsDTO(valueDetail)).collect(Collectors.toList());
    }
    private ValueDetailsDTO mapToValueDetailsDTO (ValueDetails valueDetails) {
        return ValueDetailsDTO.builder()
                .parameterValue(valueDetails.getParameterValue())
                .parameterName(valueDetails.getParameterName())
                .styleID(valueDetails.getStyleID())
                .build();
    }
}
