package com.swp391.JewelryProduction.services.finalQuotation;

import com.swp391.JewelryProduction.dto.FinalQuotationDTO;
import com.swp391.JewelryProduction.pojos.FinalQuotation;
import com.swp391.JewelryProduction.repositories.FinalQuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinalQuotationService implements IFinalQuotationService{
    FinalQuotationRepository finalQuotationRepository;

    @Autowired
    public FinalQuotationService(FinalQuotationRepository finalQuotationRepository) {
        this.finalQuotationRepository = finalQuotationRepository;
    }

    @Override
    public List<FinalQuotationDTO> findAllFinalQuotations() {
//        return finalQuotationRepository.findAll().stream().map(finalQuotation -> mapToFinalQuotationDTO(finalQuotation)).collect(Collectors.toList());
        return null;
    }

}
