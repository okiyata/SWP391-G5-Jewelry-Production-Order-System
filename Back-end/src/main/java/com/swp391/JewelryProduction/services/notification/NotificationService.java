package com.swp391.JewelryProduction.services.notification;

import com.swp391.JewelryProduction.pojos.Notification;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface NotificationService {
    void saveNotification(Notification notification);
//    Flux<ServerSentEvent<Notification>> subscribe(String accountID);
}
