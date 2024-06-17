package com.swp391.JewelryProduction.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.swp391.JewelryProduction.pojos.Quotation;
import com.swp391.JewelryProduction.services.PaypalService;
import com.swp391.JewelryProduction.services.email.EmailService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/payment")
public class PaypalController {

    private final PaypalService paypalService;

    private final APIContext apiContext;
    private final EmailService emailService;

    @Value("${base.url}")
    private String baseURL;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/create")
    public RedirectView createPayment(
            @RequestBody Quotation quotation,
            @RequestParam("cancelURL") String cancelURL,
            @RequestParam("successURL") String successURL,
            @RequestParam("errorURL") String errorURL
    ) {
        try {
            Payment payment = paypalService.makePayment(
                    quotation.getTotalPrice(),
                    "USD",
                    "Paypal Wallet payment",
                    "sale",
                    quotation.getTitle(),
                    cancelURL,
                    successURL
            );

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    log.info("Endpoint /api/payment/create: redirect to " + links.getHref());
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return new RedirectView(errorURL);
    }

    @GetMapping("/success")
    public ResponseEntity<Response> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            log.info("Endpoint /api/payment/success: Successful payment execution for paymentID " + paymentId + " of payerID " + payerId);
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Payment is done and your fucking money is gone")
                .response("paymentId", paymentId)
                .buildEntity();
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "paymentError";
    }

    @GetMapping("/receipt")
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
