package com.jez.controller;

import com.jez.dto.CreateMessageRequest;
import com.jez.entity.MessageEntity;
import com.jez.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageEntity> createMessage(@RequestBody CreateMessageRequest request) {
        MessageEntity savedMessage = messageService.processAndSendMessage(request);
        return ResponseEntity.ok(savedMessage);
    }
}