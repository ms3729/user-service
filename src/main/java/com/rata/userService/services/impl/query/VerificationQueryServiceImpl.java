package com.rata.userService.services.impl.query;

import com.rata.userService.models.Verification;
import com.rata.userService.repositories.mysql.VerificationRepository;
import com.rata.userService.services.interfaces.query.VerificationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VerificationQueryServiceImpl implements VerificationQueryService {

    private final VerificationRepository verificationRepository;


    @Override
    public Optional<Verification> find(Long id) {
        return verificationRepository.findById(id);
    }

    @Override
    public List<Verification> findAll() {
        return verificationRepository.findAll();
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
