package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.ReportDTO;

import java.util.List;

public interface IReportService {
    List<ReportDTO> findAllReports();
}
