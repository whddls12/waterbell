package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.log.ResponseLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class ResponseLogRepository {

    private final EntityManager em;

    public void save(ResponseLog responseLog) {
        em.persist(responseLog);
    }


    public long getResponseLogCnt(Facility facility, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        return em.createQuery("select COUNT(r.id) from ResponseLog r " +
                "where r.facility = :facility and r.responseTime >= :searchStartDate and r.responseTime <= :searchEndDate order by r.responseTime DESC", Long.class)
                .setParameter("facility",facility)
                .setParameter("searchStartDate",searchStartDate)
                .setParameter("searchEndDate",searchEndDate)
                .getSingleResult();
    }

    public List<ResponseLog> getLogList(Facility facility, int start, int size, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        return em.createQuery("select r from ResponseLog r " +
                "where r.facility = :facility and r.responseTime >= :searchStartDate and r.responseTime <= :searchEndDate order by r.responseTime DESC",ResponseLog.class)
                .setParameter("facility",facility)
                .setParameter("searchStartDate",searchStartDate)
                .setParameter("searchEndDate",searchEndDate)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

}
