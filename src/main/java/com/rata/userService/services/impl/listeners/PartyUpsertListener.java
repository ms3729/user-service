package com.rata.userService.services.impl.listeners;

import com.rabbitmq.client.Channel;
import com.rata.userService.config.MQConfig;
import com.rata.userService.errorHandling.BusinessException;
import com.rata.userService.errorHandling.DuplicateResourceException;
import com.rata.userService.errorHandling.InvalidMessageException;
import com.rata.userService.models.party.Party;
import com.rata.userService.records.newRecords.PartyUpsertMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class PartyUpsertListener {

    private final PartyUpsertHandler partyUpsertHandler;

    @RabbitListener(queues = MQConfig.USER_SERVICE_PARTY_QUEUE)
    public void onMessage(PartyUpsertMessage message, Channel channel,
                          @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("دریافت پیام ایجاد/به‌روزرسانی Party از {}: {}", message.sourceService(), message.sourceEntityId());

            Party party = partyUpsertHandler.handle(message);

            log.info("Party با شناسه {} با موفقیت ایجاد/به‌روزرسانی شد", party.getId());

            channel.basicAck(deliveryTag, false);

        } catch (DuplicateResourceException e) {
            log.warn("پیام تکراری است: {}", e.getMessage());
            channel.basicAck(deliveryTag, false);

        } catch (InvalidMessageException | BusinessException e) {
            log.error("پیام نامعتبر است: {}", e.getMessage());
            channel.basicNack(deliveryTag, false, false);

        } catch (Exception e) {
            log.error("خطای غیرمنتظره در پردازش پیام: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}