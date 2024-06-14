package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.services.account.AccountService;
import com.swp391.JewelryProduction.services.notification.NotificationService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @GetMapping("/{accountId}/get-all")
    public ResponseEntity<Response> getAllNotifications(@PathVariable("accountId") String receiverId) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("notification-list", notificationService.findAllByReceiver_Id(receiverId))
                .buildEntity();
    }

//    @PatchMapping("/read/{notificationID}")
//    public ResponseEntity<Response> changeStatusToRead (@PathVariable("notificationID") UUID notificationID) {
//        return Response.builder()
//                .message("Notification change status to read")
//                .response(String.valueOf(notificationID), notificationService.updateStatusToRead(notificationID))
//                .buildEntity();
//    }
}
