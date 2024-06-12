package com.swp391.JewelryProduction.repositories;

import com.swp391.JewelryProduction.pojos.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByReceiver_Id(String id);
}
