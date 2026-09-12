package com.rata.userService.repositories.mysql;

import com.rata.userService.models.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Verification findByIdentifyCodeAndCodeAndDataAndSendDateBetween(String identifiedCode, String smsCode,String data, Date start,Date end);

    Verification findFirstByDataAndSendDateBetweenOrderBySendDateDesc(String data, Date start,Date end);
}
