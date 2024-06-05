package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.ReportDTO;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.repositories.AccountRepository;
import com.swp391.JewelryProduction.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private ReportRepository reportRepository;
    private AccountRepository accountRepository;

    public void saveReport(Report report) {
        reportRepository.save(report);
    }
}
