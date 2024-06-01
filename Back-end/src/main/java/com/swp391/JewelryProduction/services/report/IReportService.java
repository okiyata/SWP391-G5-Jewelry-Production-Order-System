package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.ReportDTO;
import com.swp391.JewelryProduction.pojos.Report;

import java.util.List;

public interface IReportService {

    List<ReportDTO> findAllReports();

    ReportDTO createReport(ReportDTO reportDTO);

}
