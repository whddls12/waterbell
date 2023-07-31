package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.sms.SmsLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class SmsLogRepository {

    private final EntityManager em;

    public void save(SmsLog smsLog) {
        em.persist(smsLog);
    }
}
