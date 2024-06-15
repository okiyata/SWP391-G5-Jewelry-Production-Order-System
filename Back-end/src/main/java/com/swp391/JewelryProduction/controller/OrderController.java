package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.RequestDTOs.StaffGroup;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.pojos.Design;
import com.swp391.JewelryProduction.pojos.Order;
import com.swp391.JewelryProduction.pojos.Quotation;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.account.StaffService;
import com.swp391.JewelryProduction.services.email.EmailService;
import com.swp391.JewelryProduction.services.order.OrderService;
import com.swp391.JewelryProduction.util.Response;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final StaffService staffService;

    @GetMapping("/list")
    public ResponseEntity<Response> list() {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("order-list", orderService.findAllOrders())
                .buildEntity();
    }

    @GetMapping("/{orderId}/detail")
    public ResponseEntity<Response> getDetail(@PathVariable("orderId") String orderId) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("order-detail", orderService.findOrderById(orderId))
                .response("sale-staffs", staffService.findAllByRole(Role.SALE_STAFF))
                .response("design-staffs", staffService.findAllByRole(Role.DESIGN_STAFF))
                .response("production-staffs", staffService.findAllByRole(Role.PRODUCTION_STAFF))
                .buildEntity();
    }

    @PostMapping("/{orderId}/detail/assign-staff")
    public ResponseEntity<Response> assignStaff(@PathVariable("orderId") String orderId, @RequestBody StaffGroup staffGroup) throws MessagingException {
        Order order = orderService.findOrderById(orderId);
        order.setSaleStaff(staffGroup.getSaleStaff());
        order.setDesignStaff(staffGroup.getDesignStaff());
        order.setProductionStaff(staffGroup.getProductionStaff());
        orderService.updateOrder(order);
        //NOTIFY TO STAFF GROUP THROUGH EMAIL AND WEB NOTIFICATION
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .buildEntity();
    }

    @PostMapping("/{orderId}/detail/edit-quote")
    public ResponseEntity<Response> editQuote(@PathVariable("orderId") String orderId, @RequestBody Quotation quotation) {
        Order order = orderService.findOrderById(orderId);
        order.setQuotation(quotation);
        orderService.updateOrder(order);
        //NOTIFY TO MANAGER THROUGH EMAIL AND WEB NOTIFICATION
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .buildEntity();
    }

    @PostMapping("/{orderId}/detail/edit-design")
    public ResponseEntity<Response> editDesign(@PathVariable("orderId") String orderId, @RequestBody Design design) {
        Order order = orderService.findOrderById(orderId);
        order.setDesign(design);
        orderService.updateOrder(order);
        //NOTIFY TO MANAGER THROUGH EMAIL AND WEB NOTIFICATION
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .buildEntity();
    }
}
