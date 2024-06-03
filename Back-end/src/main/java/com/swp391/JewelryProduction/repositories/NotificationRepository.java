package com.swp391.JewelryProduction.repositories;

import com.swp391.JewelryProduction.pojos.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
}
