package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.Alarm;
import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.facility.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FloodAlarmLogRepository {

    private  final EntityManager em;

    public void save(FloodAlarmLog floodAlarmLog){
        em.persist(floodAlarmLog);
    }

    public FloodAlarmLog findById(Long id){
        return em.find(FloodAlarmLog.class, id);
    }

    public Long getSendWebLogCnt(Facility facility, int member_id, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        return em.createQuery("select count(f.id) from FloodAlarmLog f " +
                        "where f.facility = :facility and f.member.id = :member_id and f.regDate >= :searchStartDate and f.regDate <= :searchEndDate", Long.class)
                .setParameter("facility", facility)
                .setParameter("member_id", member_id)
                .setParameter("searchStartDate", searchStartDate)
                .setParameter("searchEndDate", searchEndDate)
                .getSingleResult();
    }

    public List<FloodAlarmLog> getSendWebLogList(Facility facility, int member_id, int start, int size, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        return em.createQuery("select f from FloodAlarmLog f " +
                        "where f.facility = :facility and f.member.id = :member_id and f.regDate >= :searchStartDate and f.regDate <= :searchEndDate order by f.regDate DESC", FloodAlarmLog.class)
                .setParameter("facility", facility)
                .setParameter("member_id", member_id)
                .setParameter("searchStartDate",searchStartDate)
                .setParameter("searchEndDate", searchEndDate)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }
}
