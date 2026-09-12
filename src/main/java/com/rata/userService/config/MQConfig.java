package com.rata.userService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String PARTY_EXCHANGE = "party.exchange";
    public static final String PARTY_DLX = "party.dlx";

    // Queue
    public static final String USER_SERVICE_PARTY_QUEUE = "user-service.party-upsert";
    public static final String USER_SERVICE_PARTY_DLQ = "user-service.party-upsert.dlq";

    // Routing Keys
    public static final String ROUTING_EMPLOYEE = "party.upsert.employee";
    public static final String ROUTING_DRIVER = "party.upsert.driver";
    public static final String ROUTING_FLEET_OWNER = "party.upsert.fleet-owner";
    public static final String ROUTING_CARGO_OWNER = "party.upsert.cargo-owner";
    public static final String ROUTING_BILL_ISSUER = "party.upsert.bill-issuer";
    public static final String ROUTING_CUSTOMER = "party.upsert.customer";

    // Routing Pattern
    public static final String PARTY_ROUTING_PATTERN = "party.upsert.#";

    @Bean
    public TopicExchange partyExchange() {
        return new TopicExchange(PARTY_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange partyDlx() {
        return new TopicExchange(PARTY_DLX, true, false);
    }

    @Bean
    DirectExchange superDateExchange() {
        return new DirectExchange("rpc-super-data-direct-exchange");
    }

    @Bean
    DirectExchange userExchange() {
        return new DirectExchange("rpc-user-direct-exchange");
    }

    @Bean
    public Queue userServicePartyQueue() {
        return QueueBuilder.durable(USER_SERVICE_PARTY_QUEUE)
                .deadLetterExchange(PARTY_DLX)
                .deadLetterRoutingKey("party-upsert-dead")
                .build();
    }

    @Bean
    public Queue userServicePartyDlq() {
        return QueueBuilder.durable(USER_SERVICE_PARTY_DLQ).build();
    }

    @Bean
    public Binding userServicePartyBinding() {
        return BindingBuilder
                .bind(userServicePartyQueue())
                .to(partyExchange())
                .with(PARTY_ROUTING_PATTERN);
    }

    @Bean
    public Binding userServicePartyDlqBinding() {
        return BindingBuilder
                .bind(userServicePartyDlq())
                .to(partyDlx())
                .with("party-upsert-dead");
    }

    /**
     * message
     */
    public static final String MESSAGE_QUEUE = "message_queue";
    public static final String MESSAGE_EXCHANGE = "message_exchange";
    public static final String MESSAGE_ROUTING_KEY = "message_routingKey";

    @Bean
    public Queue messageQueue() {
        return new Queue(MESSAGE_QUEUE);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(MESSAGE_EXCHANGE);
    }

    @Bean
    public Binding bindingNotification(@Qualifier("messageQueue") Queue queue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(queue).to(notificationExchange).with(MESSAGE_ROUTING_KEY);
    }
    /**
     * bank
     */
    @Bean
    public Queue superDataQueueBankId() {
        return new Queue("rpc.super.data.bank.id.request", true);
    }
    @Bean
    public Queue superDataQueueBankCode() {
        return new Queue("rpc.super.data.bank.code.request", true);
    }

    @Bean
    Binding superDataBindingBankId(@Qualifier("superDataQueueBankId") Queue superDataQueueBankId, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueBankId).to(superDateExchange).with(superDataQueueBankId.getName());
    }
    @Bean
    Binding superDataBindingBankCode(@Qualifier("superDataQueueBankCode") Queue superDataQueueBankCode, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueBankCode).to(superDateExchange).with(superDataQueueBankCode.getName());
    }

    /**
     * city
     */
    @Bean
    public Queue superDataQueueCityId() {
        return new Queue("rpc.super.data.city.id.request", true);
    }
    @Bean
    public Queue superDataQueueCityCode() {
        return new Queue("rpc.super.data.city.code.request", true);
    }

    @Bean
    Binding superDataBindingCityId(@Qualifier("superDataQueueCityId") Queue superDataQueueCityId, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueCityId).to(superDateExchange).with(superDataQueueCityId.getName());
    }
    @Bean
    Binding superDataBindingCityCode(@Qualifier("superDataQueueCityCode") Queue superDataQueueCityCode, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueCityCode).to(superDateExchange).with(superDataQueueCityCode.getName());
    }

    /**
     * state
     */
    @Bean
    public Queue superDataQueueStateId() {
        return new Queue("rpc.super.data.state.id.request", true);
    }
    @Bean
    public Queue superDataQueueStateCode() {
        return new Queue("rpc.super.data.state.code.request", true);
    }

    @Bean
    Binding superDataBindingStateId(@Qualifier("superDataQueueStateId") Queue superDataQueueStateId, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueStateId).to(superDateExchange).with(superDataQueueStateId.getName());
    }
    @Bean
    Binding superDataBindingStateCode(@Qualifier("superDataQueueStateCode") Queue superDataQueueStateCode, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueStateCode).to(superDateExchange).with(superDataQueueStateCode.getName());
    }

    /**
     * country
     */
    @Bean
    public Queue superDataQueueCountryId() {
        return new Queue("rpc.super.data.country.id.request", true);
    }
    @Bean
    public Queue superDataQueueCountryCode() {
        return new Queue("rpc.super.data.country.code.request", true);
    }

    @Bean
    Binding superDataBindingCountryId(@Qualifier("superDataQueueCountryId") Queue superDataQueueCountryId, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueCountryId).to(superDateExchange).with(superDataQueueCountryId.getName());
    }
    @Bean
    Binding superDataBindingCountryCode(@Qualifier("superDataQueueCountryCode") Queue superDataQueueCountryCode, DirectExchange superDateExchange) {
        return BindingBuilder.bind(superDataQueueCountryCode).to(superDateExchange).with(superDataQueueCountryCode.getName());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setReplyTimeout(15000);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper jsonMapper) {
        return new Jackson2JsonMessageConverter(jsonMapper);
    }
}