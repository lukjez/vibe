package com.jez.service;

import com.jez.dto.CreateMessageRequest;
import com.jez.dto.MessageDto;
import com.jez.entity.MessageEntity;
import com.jez.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.messages}")
    private String messagesTopic;

    public MessageService(MessageRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public MessageEntity processAndSendMessage(CreateMessageRequest request) {
        // 1. Zapis rekordu ze statusem NEW
        MessageEntity message = new MessageEntity();
        message.setTopic(request.topic());
        message.setContent(request.content());
        message.setStatus(MessageEntity.MessageStatus.NEW);

        message = repository.save(message);

        try {
            // 2. Wysyłka do Kafki
            MessageDto dto = new MessageDto(message.getId(), message.getTopic(), message.getContent());
            kafkaTemplate.send(messagesTopic, message.getId().toString(), dto);

            // 3. Po udanej wysyłce zmiana statusu na PENDING
            message.setStatus(MessageEntity.MessageStatus.PENDING);
            return repository.save(message);

        } catch (Exception e) {
            // W przypadku błędu przy samej wysyłce do Kafki -> FAILED
            message.setStatus(MessageEntity.MessageStatus.FAILED);
            return repository.save(message);
        }
    }

    @Transactional
    public void updateStatus(Long id, String statusStr) {
        repository.findById(id).ifPresent(message -> {
            try {
                MessageEntity.MessageStatus status = MessageEntity.MessageStatus.valueOf(statusStr);
                message.setStatus(status);
                repository.save(message);
            } catch (IllegalArgumentException e) {
                message.setStatus(MessageEntity.MessageStatus.FAILED);
                repository.save(message);
            }
        });
    }
}