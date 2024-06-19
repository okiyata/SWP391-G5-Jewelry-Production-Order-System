package com.swp391.JewelryProduction.services.parameterValue;

import com.swp391.JewelryProduction.dto.ParameterValueDTO;
import com.swp391.JewelryProduction.repositories.ParameterValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterValueServiceImpl implements ParameterValueService {
    private ParameterValueRepository parameterValueRepository;

    @Autowired
    public ParameterValueServiceImpl(ParameterValueRepository parameterValueRepository) {
        this.parameterValueRepository = parameterValueRepository;
    }

    @Override
    public List<ParameterValueDTO> findAllParameterValues() {
//        return parameterValueRepository.findAll().stream().map(parameterValue -> mapToParameterValueDTO(parameterValue)).collect(Collectors.toList());
        return null;
    }
}
