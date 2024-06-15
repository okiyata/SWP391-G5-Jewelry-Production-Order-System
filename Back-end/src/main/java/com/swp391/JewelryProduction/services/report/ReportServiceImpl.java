package com.swp391.JewelryProduction.services.report;

import com.swp391.JewelryProduction.dto.ReportDTO;
import com.swp391.JewelryProduction.dto.RequestDTOs.ReportRequest;
import com.swp391.JewelryProduction.enums.ReportType;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.repositories.AccountRepository;
import com.swp391.JewelryProduction.repositories.ReportRepository;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    public Report saveReport(Report report) {
        reportRepository.save(report);
        return report;
    }

    @Override
    public Report createRequest(ReportRequest request, Order order) {
        Account sender = modelMapper.map(accountService.findAccountByEmail(request.getSenderEmail()), Account.class);
        Report report = Report.builder()
                        .reportingOrder(order)
                        .title(request.getTitle())
                        .createdDate(LocalDateTime.now())
                        .type(ReportType.REQUEST)
                        .sender(sender)
                        //STATE MACHINE FOR NOTIFICATION
                        .build();

        order.getRelatedReports().add(report);
        orderService.updateOrder(order);
        return report;
    }

    public Report createReport(ReportRequest request, Order order) {
        Account sender = modelMapper.map(accountService
                .findAccountByEmail(request.getSenderEmail()), Account.class);
        Report report = Report.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .createdDate(LocalDateTime.now())
                .sender(sender)
                .build();
        return report;
    }
}
