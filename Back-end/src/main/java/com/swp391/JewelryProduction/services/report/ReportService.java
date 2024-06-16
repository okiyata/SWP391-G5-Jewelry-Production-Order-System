package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.ReportDTO;
import com.swp391.JewelryProduction.dto.RequestDTOs.ReportRequest;
import com.swp391.JewelryProduction.pojos.*;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ReportService {

    Report saveReport(Report report);
    Report createRequest(ReportRequest report, Order order);
    Report createQuotationReport(ReportRequest report, Order order);
    Report createDesignReport(ReportRequest report, Order order);
    void handleUserResponse(String orderId, String response) throws Exception;
    Report createNormalReport(Order order, String title, String content);
    Report findReportByID (Integer id);
}
