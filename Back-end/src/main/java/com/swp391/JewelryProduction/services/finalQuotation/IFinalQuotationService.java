package com.swp391.JewelryProduction.services.finalQuotation;

import com.swp391.JewelryProduction.dto.FinalQuotationDTO;

import java.util.List;

public interface IFinalQuotationService {
    List<FinalQuotationDTO> findAllFinalQuotations();
}
