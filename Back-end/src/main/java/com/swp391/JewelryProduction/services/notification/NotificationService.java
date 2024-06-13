package com.swp391.JewelryProduction.services.notification;

import com.swp391.JewelryProduction.pojos.Notification;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void saveNotification(Notification notification);
//    Flux<ServerSentEvent<Notification>> subscribe(String accountID);
    List<Notification> findAllByReceiver_Id(String receiverId);
    Notification findById(UUID id);
    void deleteNotification(UUID id);
}
