package com.swp391.JewelryProduction.services.quotationItem;

import com.swp391.JewelryProduction.dto.QuotationItemDTO;
import com.swp391.JewelryProduction.pojos.QuotationItem;

import java.util.List;

public interface IQuotationItemService {
    List<QuotationItemDTO> findAllQuotationItems();
}
