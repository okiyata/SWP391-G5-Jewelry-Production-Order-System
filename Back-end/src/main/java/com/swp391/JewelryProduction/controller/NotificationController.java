package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.pojos.Notification;
import com.swp391.JewelryProduction.services.notification.NotificationService;
import com.swp391.JewelryProduction.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


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

    @PostMapping("/{notificationId}/get")
    public ResponseEntity<Response> getNotification(@PathVariable("notificationId") UUID notificationId) {
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .response("notification", notificationService.findById(notificationId))
                .buildEntity();
    }

    @PostMapping("/{notificationId}/remove")
    public ResponseEntity<Response> removeNotification(@PathVariable("notificationId") UUID notificationId, @PathVariable("accountId") String receiverId) {
        notificationService.deleteNotification(notificationId);
        return Response.builder()
                .status(HttpStatus.OK)
                .message("Request send successfully.")
                .buildEntity();
    }
}
