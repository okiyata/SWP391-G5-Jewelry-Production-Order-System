package com.swp391.JewelryProduction.services.nofitcation;

import com.swp391.JewelryProduction.pojos.Notification;
import com.swp391.JewelryProduction.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}
