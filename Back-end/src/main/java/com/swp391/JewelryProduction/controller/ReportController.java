package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.enums.OrderStatus;
import com.swp391.JewelryProduction.enums.ReportType;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.enums.WorkStatus;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Report;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.report.ReportService;
import com.swp391.JewelryProduction.util.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@NoArgsConstructor
public class ReportController {
    private ReportService reportService;
    private AccountService accountService;


    @PostMapping
    public void getReport(Report report, Order order) {
        switch (report.getType()) {
            case REQUEST:
                order = new Order();
                order.setStatus(OrderStatus.REQUESTING);
                order.getReports().add(report);
                accountService.findAccountByRole(Role.MANAGER).notify(); //notify the manager with the request attached, notify() needs to be replace for another method
            case QUOTATION: //quotation has to send to the manager and the customer for approval
                order.setStatus(OrderStatus.QUO_AWAIT_APPROVAL);
                accountService.findAccountByRole(Role.MANAGER).notify(); //notify the manager with the quotation attached, notify() needs to be replace for another method
            case ORDER: //after manager approved the quotation and set to order and the customer purchased the quotation
                order.setStatus(OrderStatus.IN_DESIGNING);
                order.getReports().add(report);
                //notify the designer with the report(type: order) attached
                accountService.findStaffByRoleAndWorkStatus(Role.DESIGN_STAFF, WorkStatus.FREE).notify(); //report with ReportType.ORDER
        }

    }
}
