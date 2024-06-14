package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.RequestDTOs.ReportRequest;
import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.report.ReportService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/request")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @PostMapping("/{accountId}/create")
    public ResponseEntity<Response> createRequest(@RequestBody ReportRequest request, @PathVariable String accountId) {

    }
}
