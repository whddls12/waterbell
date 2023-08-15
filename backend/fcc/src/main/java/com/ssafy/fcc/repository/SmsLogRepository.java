package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.sms.SmsLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SmsLogRepository {

    private final EntityManager em;

    public void save(SmsLog smsLog) {
        em.persist(smsLog);
    }

    public SmsLog findById(Long id) {
        return em.find(SmsLog.class, id);
    }

    public Long getSendSmsLogCnt(int member_id, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        return em.createQuery("select count(s.id) from SmsLog s " +
                        "where s.member.id = :member_id and s.sendDate >= :searchStartDate and s.sendDate <= :searchEndDate", Long.class)
                .setParameter("member_id", member_id)
                .setParameter("searchStartDate", searchStartDate)
                .setParameter("searchEndDate", searchEndDate)
                .getSingleResult();
    }

    public List<SmsLog> getSendSmsLogList(int member_id, int start, int size, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        return em.createQuery("select s from SmsLog s " +
                        "where s.member.id = :member_id and s.sendDate >= :searchStartDate and s.sendDate <= :searchEndDate order by s.sendDate DESC", SmsLog.class)
                .setParameter("member_id", member_id)
                .setParameter("searchStartDate",searchStartDate)
                .setParameter("searchEndDate", searchEndDate)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }
}
