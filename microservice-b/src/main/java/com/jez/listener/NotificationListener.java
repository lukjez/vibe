package com.jez.listener;

import com.jez.dto.MessageDto;
import com.jez.dto.StatusUpdateDto;
import com.jez.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final EmailService emailService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.status}")
    private String statusTopic;

    public NotificationListener(EmailService emailService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.emailService = emailService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${app.kafka.topic.messages}", groupId = "notification-group")
    public void consumeMessage(MessageDto message) {
        try {
            // Wysyłka maila
            emailService.sendEmail(message.topic(), message.content());

            // Sukces -> Odpowiedź do Kafki o statusie SENT
            sendStatusUpdate(message.id(), "SENT");

        } catch (Exception e) {
            // Błąd -> Odpowiedź do Kafki o statusie FAILED
            sendStatusUpdate(message.id(), "FAILED");
        }
    }

    private void sendStatusUpdate(Long id, String status) {
        StatusUpdateDto update = new StatusUpdateDto(id, status);
        kafkaTemplate.send(statusTopic, id.toString(), update);
    }
}