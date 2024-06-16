package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.RequestDTOs.ReportRequest;
import com.swp391.JewelryProduction.pojos.Quotation;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.notification.NotificationService;
import com.swp391.JewelryProduction.services.order.OrderService;
import com.swp391.JewelryProduction.services.product.ProductService;
import com.swp391.JewelryProduction.services.report.ReportService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final OrderService orderService;
    private final ReportService reportService;
    private final ProductService productService;

    @PostMapping("/{accountId}/{productSpecId}/create/request")
    public ResponseEntity<Response> createRequest(
            @RequestBody ReportRequest request,
            @PathVariable("productSpecId") String specId,
            @PathVariable("accountId") String accountId)
    {
        request.setReportContentID(specId);
        request.setSenderId(accountId);
        reportService.createRequest(request, orderService.saveNewOrder(accountId));
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully.")
                .buildEntity();
    }

//    @PostMapping("/{accountId}/{orderId}/create/quote")
//    public ResponseEntity<Response> createQuoteReport(
//            @RequestBody ReportRequest quoteRequest,
//            @PathVariable("accountId") String accountId)
//    {
//        quoteRequest.setReportContentID(quote);
//    }
}
