package com.rata.userService.services.impl.command;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rata.userService.config.MQConfig;
import com.rata.userService.dto.MessageDTO;
import com.rata.userService.enums.MessageLevel;
import com.rata.userService.enums.MessageType;
import com.rata.userService.services.interfaces.command.MessageCommandService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageCommandServiceImpl implements MessageCommandService {

    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;

    @Override
    public void sendChangePermissionNotification(String username) {
        try {
            MessageDTO message = new MessageDTO();
            message.setTemplateId(4);
            message.setChannel("web_notification");
            message.setDataType("WRANING");
            message.setLevel(MessageLevel.PRIVATE.toString());
            message.setType(MessageType.info.name());
            message.setTarget(username);
            template.convertAndSend(MQConfig.MESSAGE_EXCHANGE, MQConfig.MESSAGE_ROUTING_KEY, objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSms(MessageDTO message) {
        try {
            message.setChannel("sms");
            message.setDataType("INFO");
            template.convertAndSend(MQConfig.MESSAGE_EXCHANGE, MQConfig.MESSAGE_ROUTING_KEY, objectMapper.writeValueAsString(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
