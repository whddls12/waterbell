package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.facility.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardAlarmLogRepository {

    private final EntityManager em;

    public void save(BoardAlarmLog boardAlarmLog) {
        em.persist(boardAlarmLog);
    }

    public Long getSendWebLogCnt(Facility facility, int member_id, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        return em.createQuery("select count(b.id) from BoardAlarmLog b " +
                        "where b.facility = :facility and b.member.id = :member_id and b.regDate >= :searchStartDate and b.regDate <= :searchEndDate", Long.class)
                .setParameter("facility", facility)
                .setParameter("member_id", member_id)
                .setParameter("searchStartDate", searchStartDate)
                .setParameter("searchEndDate", searchEndDate)
                .getSingleResult();
    }

    public List<BoardAlarmLog> getSendWebLogList(Facility facility, int member_id, int start, int size, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        return em.createQuery("select b from BoardAlarmLog b " +
                        "where b.facility = :facility and b.member.id = :member_id and b.regDate >= :searchStartDate and b.regDate <= :searchEndDate order by b.regDate DESC", BoardAlarmLog.class)
                .setParameter("facility", facility)
                .setParameter("member_id", member_id)
                .setParameter("searchStartDate",searchStartDate)
                .setParameter("searchEndDate", searchEndDate)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }
}
