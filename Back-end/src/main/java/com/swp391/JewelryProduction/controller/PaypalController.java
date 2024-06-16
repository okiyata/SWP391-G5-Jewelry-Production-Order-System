package com.swp391.JewelryProduction.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.swp391.JewelryProduction.services.PaypalService;
import com.swp391.JewelryProduction.services.email.EmailService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;
    private final APIContext apiContext;
    private final EmailService emailService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.makePayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public ResponseEntity<Response> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Payment is done and your fucking money is gone")
                .response("paymentId", paymentId)
                .buildEntity();
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }

    @GetMapping("/payment/receipt")
    public void generateReceipt(@RequestBody String paymentId) {
        Payment payment = null;
        try {
            payment = paypalService.getPaymentDetails(paymentId);
            List<Transaction> transactions = payment.getTransactions();
            if (!transactions.isEmpty()) {
                Transaction transaction = transactions.getFirst();
                List<RelatedResources> relatedResources = transaction.getRelatedResources();
                if (!relatedResources.isEmpty()) {
                    Sale sale = paypalService.getSaleDetails(relatedResources.getFirst().getSale().getId());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
    }
}
