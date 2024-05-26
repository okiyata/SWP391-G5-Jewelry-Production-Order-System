package com.swp391.JewelryProduction.services.message;

import com.swp391.JewelryProduction.dto.MessageDTO;
import com.swp391.JewelryProduction.pojos.Message;
import com.swp391.JewelryProduction.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService implements IMessageService{
    private MessageRepository messageRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Override
    public List<MessageDTO> findAllMessages() {
        return messageRepository.findAll().stream().map(message -> mapToMessageDTO(message)).collect(Collectors.toList());
    }

    private MessageDTO mapToMessageDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .build();
    }
}
