package com.rata.userService.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rata.userService.dto.ZoneFilter;
import com.rata.userService.records.superData.BankRecord;
import com.rata.userService.records.superData.ZoneRecord;
import com.rata.userService.services.interfaces.SuperDataService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SuperDataServiceImpl implements SuperDataService {

    private final RabbitTemplate amqpTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public BankRecord findBankById(int id) {
        String result = (String) amqpTemplate.convertSendAndReceive("rpc-super-data-direct-exchange", "rpc.super.data.bank.id.request", id);
        return bankBuilder(result);
    }

    @Override
    public BankRecord findBankByCode(String code) {
        String result = null;
        try {
            result = (String) amqpTemplate.convertSendAndReceive("rpc-super-data-direct-exchange", "rpc.super.data.bank.code.request", code);
        } catch (Exception ex) {
        }
        return result != null ? bankBuilder(result) : null;
    }

    @Override
    public ZoneRecord findCityById(int id) {
        String record = null;
        try {
            ZoneFilter filter = new ZoneFilter(id, "fa");
            record = (String) amqpTemplate.convertSendAndReceive("rpc-super-data-direct-exchange", "rpc.super.data.city.id.request", objectMapper.writeValueAsString(filter));
        } catch (Exception ex) {

        }
        return record != null ? zoneBuilder(record) : null;
    }

    @Override
    public ZoneRecord findStateById(int id) {
        String record = null;
        try {
            ZoneFilter filter = new ZoneFilter(id, "fa");
            record = (String) amqpTemplate.convertSendAndReceive("rpc-super-data-direct-exchange", "rpc.super.data.state.id.request", objectMapper.writeValueAsString(filter));
        } catch (Exception ex) {
        }
        return record != null ? zoneBuilder(record) : null;
    }

    @Override
    public ZoneRecord findCountryById(int id) {
        String record = null;
        try {
            ZoneFilter filter = new ZoneFilter(id, "fa");
            record = (String) amqpTemplate.convertSendAndReceive("rpc-super-data-direct-exchange", "rpc.super.data.country.id.request", objectMapper.writeValueAsString(filter));
        } catch (Exception ex) {
        }
        return record != null ? zoneBuilder(record) : null;
    }

    private BankRecord bankBuilder(String responseData) {
        BankRecord record = null;
        try {
            record = objectMapper.readValue(responseData, BankRecord.class);
        } catch (JsonProcessingException ex) {
        }
        return record;
    }

    private ZoneRecord zoneBuilder(String responseData) {
        ZoneRecord record = null;
        try {
            record = objectMapper.readValue(responseData, ZoneRecord.class);
        } catch (JsonProcessingException ex) {
        }
        return record;
    }

}
