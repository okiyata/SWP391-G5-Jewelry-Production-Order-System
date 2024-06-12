package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.pojos.Notification;
import com.swp391.JewelryProduction.services.notification.NotificationService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/{accountId}/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllNotifications(@PathVariable("accountId") String receiverId) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("notification-list", notificationService.findAllByReceiver_Id(receiverId))
                .buildEntity();
    }
}
