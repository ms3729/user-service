package com.rata.userService.services.impl;

import com.rata.userService.config.Utils;
import com.rata.userService.dto.MessageDTO;
import com.rata.userService.dto.VerificationDTO;
import com.rata.userService.enums.MessageLevel;
import com.rata.userService.enums.MessageType;
import com.rata.userService.models.User;
import com.rata.userService.models.Verification;
import com.rata.userService.repositories.mysql.VerificationRepository;
import com.rata.userService.services.interfaces.MessageService;
import com.rata.userService.services.interfaces.UserService;
import com.rata.userService.services.interfaces.VerificationService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final UserService userService;
    private final MessageService messageService;
    @Override
    public String save(VerificationDTO dto) {
        Optional<User> user = userService.find(dto.getUserId());
        if (user.isPresent()) {
            Verification verification = new Verification();
            verification.setMobile(user.get().getParty().getPrimaryMobile());
            verification.setUser(user.get());
            verification.setData(dto.getData());
            verification.setType(dto.getType());
            verification.setSendDate(new Date());
            verification.setCode(Utils.generateRandomCode());
            verification.setIdentifyCode(UUID.randomUUID().toString().replace("-", ""));
            verification = verificationRepository.save(verification);
            if (dto.getType().equals("payment")) {
                messageService.sendSms(createPaymentVerificationSms(verification.getMobile(), verification.getCode(), dto.getAmount(), verification.getData()));
            }
            if (dto.getType().equals("driver_performance")) {
                messageService.sendSms(createDriverPerformanceVerificationSms(verification.getMobile(), verification.getCode(), verification.getData()));
            }
            if (dto.getType().equals("settle_issuer_co")) {
                messageService.sendSms(createSettleIssuerCoVerificationSms(verification.getMobile(), verification.getCode(), verification.getData()));
            }
            return verification.getIdentifyCode();
        }
        return null;
    }

    @Override
    public String saveLogin(User user) {
        Verification verification = new Verification();
        verification.setMobile(user.getParty().getPrimaryMobile());
        verification.setUser(user);
        verification.setData(user.getUsername());
        verification.setType("LOGIN");
        verification.setSendDate(new Date());
        verification.setCode(Utils.generateRandomCode());
        verification.setIdentifyCode(UUID.randomUUID().toString().replace("-", ""));
        verification = verificationRepository.save(verification);
        messageService.sendSms(createLoginVerificationSms(verification.getMobile(), verification.getCode()));
        return verification.getIdentifyCode();
    }

    private MessageDTO createPaymentVerificationSms(String mobile, String code, String amount, String data) {
        MessageDTO message = new MessageDTO();
        message.setTemplateId(795169);
        message.setLevel(MessageLevel.PRIVATE.toString());
        message.setType(MessageType.info.name());
        message.setTarget(mobile);
        JSONObject jsonObject = new JSONObject(data);
        Map<String, String> mapData = new HashMap<>();
        mapData.put("bundle_no", jsonObject.getString("bundle_no"));
        mapData.put("carry_cargo_count", String.valueOf(jsonObject.getInt("carry_cargo_count")));
        mapData.put("code", code);
        mapData.put("amount", amount);
        message.setData(mapData);
        return message;
    }

    private MessageDTO createDriverPerformanceVerificationSms(String mobile, String code, String data) {
        MessageDTO message = new MessageDTO();
        message.setTemplateId(258667);
        message.setLevel(MessageLevel.PRIVATE.toString());
        message.setType(MessageType.info.name());
        message.setTarget(mobile);
        Map<String, String> mapData = new HashMap<>();
        mapData.put("sheet_no", data);
        mapData.put("code", code);
        message.setData(mapData);
        return message;
    }

    private MessageDTO createSettleIssuerCoVerificationSms(String mobile, String code, String data) {
        MessageDTO message = new MessageDTO();
        message.setTemplateId(463518);
        message.setLevel(MessageLevel.PRIVATE.toString());
        message.setType(MessageType.info.name());
        message.setTarget(mobile);
        Map<String, String> mapData = new HashMap<>();
        mapData.put("sheet_no", data);
        mapData.put("code", code);
        message.setData(mapData);
        return message;
    }

    private MessageDTO createLoginVerificationSms(String mobile, String code) {
        MessageDTO message = new MessageDTO();
        message.setTemplateId(332621);
        message.setLevel(MessageLevel.PRIVATE.toString());
        message.setType(MessageType.info.name());
        message.setTarget(mobile);
        Map<String, String> data = new HashMap<>();
        data.put("code", code);
        message.setData(data);
        return message;
    }
    
    @Override
    public boolean checkVerificationCode(String identifierCode, String smsCode, String data) {
        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.MINUTE, calendarEnd.get(Calendar.MINUTE) - 2);
        Verification verification = verificationRepository.findByIdentifyCodeAndCodeAndDataAndSendDateBetween(identifierCode, smsCode, data, calendarStart.getTime(), calendarEnd.getTime());
        return verification != null;
    }

    @Override
    public String checkValidityResendSms(String data) {
        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.MINUTE, calendarEnd.get(Calendar.MINUTE) - 2);
        Verification verification = verificationRepository.findFirstByDataAndSendDateBetweenOrderBySendDateDesc(data, calendarStart.getTime(), calendarEnd.getTime());
        return verification != null ? verification.getIdentifyCode() : null;
    }
}
