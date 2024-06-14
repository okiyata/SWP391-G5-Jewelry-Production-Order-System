package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Quotation;
import com.swp391.JewelryProduction.services.order.OrderService;
import com.swp391.JewelryProduction.services.quotation.QuotationService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/{orderId}/quotation")
@RequiredArgsConstructor
public class QuotationController {
    private final QuotationService quotationService;
    private final OrderService orderService;

    @GetMapping("/get-quote")
    public ResponseEntity<Response> getQuotation(@PathVariable String orderId) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("quotation", quotationService.findQuotationByOrderId(orderId))
                .buildEntity();
    }

    @PostMapping("/submit")
    public ResponseEntity<Response> createQuotation(@PathVariable String orderId, @RequestBody Quotation quotation) {
        Order order = orderService.findOrderById(orderId);
        order.setQuotation(quotation);
        orderService.updateOrder(order);
        quotationService.saveQuotation(quotation);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("quotation", quotationService.findQuotationByOrderId(orderId))
                .buildEntity();
    }

    @PostMapping("/delete")
    public ResponseEntity<Response> removeQuotation(@PathVariable String orderId, @RequestBody Quotation quotation) {
        Order order = orderService.findOrderById(orderId);
        order.setQuotation(null);
        orderService.updateOrder(order);
        quotationService.deleteQuotation(quotation);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("quotation", quotationService.findQuotationByOrderId(orderId))
                .buildEntity();
    }
}
