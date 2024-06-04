package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.ReportDTO;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.repositories.AccountRepository;
import com.swp391.JewelryProduction.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private ReportRepository reportRepository;
    private AccountRepository accountRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, AccountRepository accountRepository) {
        this.reportRepository = reportRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<ReportDTO> findAllReports() {
        return reportRepository.findAll().stream().map(report -> mapToReportDTO(report)).collect(Collectors.toList());
    }

    @Override
    public ReportDTO createReport(ReportDTO reportDTO) {
        Account account = accountRepository.findById(reportDTO.getSender().getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Report report = Report.builder()
                .title(reportDTO.getTitle())
                .description(reportDTO.getDescription())
                .createdDate(LocalDateTime.now())
                .type(reportDTO.getType())
                .sender(reportDTO.getSender())
                .build();

        return mapToReportDTO(reportRepository.save(report));
    }

    private ReportDTO mapToReportDTO(Report report) {
        return ReportDTO.builder()
                .type(report.getType())
                .id(report.getId())
                .title(report.getTitle())
                .createdDate(report.getCreatedDate())
                .description(report.getDescription())
                .sender(report.getSender())
                .build();
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }
}
