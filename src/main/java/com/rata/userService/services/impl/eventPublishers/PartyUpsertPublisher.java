package com.rata.userService.services.impl.eventPublishers;

import com.rata.userService.config.MQConfig;
import com.rata.userService.records.newRecords.PartyUpsertMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PartyUpsertPublisher {

    private final RabbitTemplate rabbitTemplate;


    public void publish(PartyUpsertMessage message, String routingKey) {
        MessagePostProcessor postProcessor = msg -> {
            msg.getMessageProperties().setMessageId(message.messageId().toString());
            msg.getMessageProperties().setHeader("sourceService", message.sourceService());
            msg.getMessageProperties().setHeader("sourceEntityType", message.sourceEntityType());
            msg.getMessageProperties().setHeader("occurredAt", message.occurredAt().toString());
            return msg;
        };

        rabbitTemplate.convertAndSend(
                MQConfig.PARTY_EXCHANGE,
                routingKey,
                message,
                postProcessor
        );
    }
}