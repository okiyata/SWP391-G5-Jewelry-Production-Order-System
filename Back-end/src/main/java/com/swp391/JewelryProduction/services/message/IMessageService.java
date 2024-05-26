package com.swp391.JewelryProduction.services.message;

import com.swp391.JewelryProduction.dto.MessageDTO;

import java.util.List;

public interface IMessageService {
    List<MessageDTO> findAllMessages();
}
