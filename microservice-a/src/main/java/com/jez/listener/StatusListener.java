package com.jez.listener;

import com.jez.dto.StatusUpdateDto;
import com.jez.service.MessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StatusListener {

    private final MessageService messageService;

    public StatusListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${app.kafka.topic.status}", groupId = "ingestion-group")
    public void handleStatusUpdate(StatusUpdateDto update) {
        messageService.updateStatus(update.id(), update.status());
    }
}
