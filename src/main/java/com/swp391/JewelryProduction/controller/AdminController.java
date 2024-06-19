package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.enums.AccountStatus;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.enums.WorkStatus;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Staff;
import com.swp391.JewelryProduction.repositories.AccountRepository;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.account.StaffService;
import com.swp391.JewelryProduction.util.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AccountService accountService;
    private final StaffService staffService;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;


    //STAFF
    @GetMapping("/get/staff-list")
    public ResponseEntity<Response> getStaffList() {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("staff-list", staffService.findAllStaff())
                .buildEntity();
    }

    @GetMapping("/get/staff-list/{staffId}")
    public ResponseEntity<Response> getStaff(@PathVariable String staffId) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("staff", staffService.findStaffById(staffId))
                .buildEntity();
    }

    @PostMapping("/get/staff-list/create")
    public ResponseEntity<Response> createStaff(@Valid @RequestBody AccountDTO accountDTO, WorkStatus workStatus) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("staff", staffService.createStaff(accountDTO, workStatus))
                .buildEntity();
    }

    @PostMapping("/get/staff-list//update")
    public ResponseEntity<Response> updateStaff(@Valid @RequestBody AccountDTO accountDTO, WorkStatus workStatus) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("staff", staffService.updateStaff(accountDTO, workStatus))
                .buildEntity();
    }

    @PostMapping("/get/staff-list/{staffId}/delete")
    public ResponseEntity<Response> deleteStaff(@PathVariable String staffId) {
        staffService.deleteStaffById(staffService.findStaffById(staffId));
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .buildEntity();
    }
    //CUSTOMER
    @GetMapping("/get/customer-list")
    public ResponseEntity<Response> getCustomerList() {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("customer-list", accountService.findAllByRole(Role.CUSTOMER))
                .buildEntity();
    }

    @GetMapping("/get/customer-list/{customerId}")
    public ResponseEntity<Response> getCustomer(@PathVariable String customerId) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("customer", accountService.findAccountById(customerId))
                .buildEntity();
    }

    @PostMapping("/get/customer-list/create")
    public ResponseEntity<Response> creaeteCustomer(@Valid @RequestBody AccountDTO accountDTO) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("customer", accountService.saveAccountIfNew(accountDTO))
                .buildEntity();
    }

    @PostMapping("/get/customer-list/update")
    public ResponseEntity<Response> updateCustomer(@Valid @RequestBody AccountDTO accountDTO) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .response("customer", accountService.updateAccount(accountDTO))
                .buildEntity();
    }

    @PostMapping("/get/customer-list/{customerID}/delete")
    public ResponseEntity<Response> deleteCustomer(@PathVariable String customerID) {
        accountRepository.delete(modelMapper.map(accountService.findAccountById(customerID), Account.class));
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request sent successfully")
                .buildEntity();
    }
}
