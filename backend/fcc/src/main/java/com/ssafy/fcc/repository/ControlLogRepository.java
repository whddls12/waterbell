package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.log.CommandType;
import com.ssafy.fcc.domain.log.ControlLog;
import com.ssafy.fcc.domain.log.ControlType;
import com.ssafy.fcc.domain.log.SensorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ControlLogRepository {

    private final EntityManager em;

    public void save(ControlLog controlLog) {
        System.out.println(controlLog.toString());
        if (controlLog != null) {
            em.persist(controlLog);
        } else {
            // controlLog 객체가 null인 경우 예외 처리 또는 로그 출력 등을 수행
            System.err.println("ControlLog object is null. Cannot save.");
        }
    }

    public List<ControlLog> getLogList(Facility facility, int start, int end, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        return em.createQuery("select c from ControlLog c " +
                        "where c.facility= :facility and c.controlTime >= :searchStartDate and c.controlTime <= :searchEndDate", ControlLog.class)
                .setParameter("facility",facility)
                .setParameter("searchStartDate",searchStartDate)
                .setParameter("searchEndDate",searchEndDate)
                .setFirstResult(start)
                .setMaxResults(end)
                .getResultList();
    }

    public Long getControlLogCnt(Facility facility) {
        return em.createQuery("select count(c.id) from ControlLog c where c.facility = :facility", Long.class)
                .setParameter("facility",facility)
                .getSingleResult();
    }

}
