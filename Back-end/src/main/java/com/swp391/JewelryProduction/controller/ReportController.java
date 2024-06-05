package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.enums.ReportType;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.Notification;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.notification.NotificationService;
import com.swp391.JewelryProduction.services.order.OrderServiceImpl;
import com.swp391.JewelryProduction.services.report.ReportServiceImpl;
import com.swp391.JewelryProduction.util.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final OrderServiceImpl orderService;
    private NotificationService notificationService;
    private ReportServiceImpl reportService;
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Response> postCustomerRequest(@Valid @RequestBody Report report) {
        report.setType(ReportType.REQUEST);
        Order order = new Order();
        order.setStatus(OrderStatus.REQUESTING);
        Notification notification = new Notification(UUID.randomUUID(), report, order, accountService.findAccountByRole(Role.MANAGER));
        reportService.saveReport(report);
        orderService.saveOrder(order);
        notificationService.saveNotification(notification);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("request", report)
                .buildEntity();
    }

    @PostMapping
    public ResponseEntity<Response> postStaffQuotation(@Valid @RequestBody Report report) {
        report.setType(ReportType.QUOTATION);
        reportService.saveReport(report);
        orderService.findAllOrders().stream().filter(ord -> {
            return ord.getId().equals(report.getReportingOrder().getId());
        }).findFirst().get().getRelatedReports().add(report);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Quotation send successfully.")
                .response("quotation", report)
                .buildEntity();
    }

    @PostMapping
    public ResponseEntity<Response> postCustomerOrder(@Valid @RequestBody Report report) {
        report.setType(ReportType.ORDER);
        reportService.saveReport(report);
        orderService.findAllOrders().stream().filter(ord -> {
            return ord.getId().equals(report.getReportingOrder().getId());
        }).findFirst().get().getRelatedReports().add(report);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Quotation send successfully.")
                .response("quotation", report)
                .buildEntity();
    }

    
}
