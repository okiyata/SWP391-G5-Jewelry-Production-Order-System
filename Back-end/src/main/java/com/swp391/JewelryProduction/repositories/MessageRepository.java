package com.swp391.JewelryProduction.repositories;

import com.swp391.JewelryProduction.pojos.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
