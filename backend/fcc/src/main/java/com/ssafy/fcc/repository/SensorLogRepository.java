package com.ssafy.fcc.repository;


import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.log.SensorLog;
import com.ssafy.fcc.domain.log.SensorType;
import com.ssafy.fcc.dto.SensorLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SensorLogRepository {

    private final EntityManager em;

    public void save(SensorLog sensorLog) {
        em.persist(sensorLog);
    }

    public int getRecentData(Facility facility, SensorType category) {
        return em.createQuery("select a from SensorLog a where a.facility = :facility and a.category = :category order by a.sensorTime DESC ", SensorLog.class)
                .setParameter("facility", facility)
                .setParameter("category", category)
                .setMaxResults(1)
                .getSingleResult()
                .getSensorData();
    }

    public List<SensorLog> getLogList(Facility facility, SensorType category, int start, int size, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        return em.createQuery("select a from SensorLog a " +
                        "where a.facility = :facility and a.category = :category and a.sensorTime >= :searchStartDate and a.sensorTime <= :searchEndDate", SensorLog.class)
                .setParameter("facility", facility)
                .setParameter("category", category)
                .setParameter("searchStartDate",searchStartDate)
                .setParameter("searchEndDate", searchEndDate)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
    }

    public Long getSensorLogCnt(Facility facility, SensorType category) {
        return em.createQuery("select count(s.id) from SensorLog s where s.facility = :facility and s.category = :category", Long.class)
                .setParameter("facility", facility)
                .setParameter("category", category)
                .getSingleResult();
    }

    public int getHeightPerhour(Facility facility, SensorType sensorType, LocalDateTime time) {
        try {
            return em.createQuery("select s from SensorLog s" +
                            " where s.facility = :facility and s.category = :sensorType and s.sensorTime <= :time ORDER BY s.sensorTime DESC ", SensorLog.class)
                    .setParameter("time", time)
                    .setParameter("facility", facility)
                    .setParameter("sensorType",sensorType)
                    .setMaxResults(1)
                    .getSingleResult()
                    .getSensorData();
        } catch (NoResultException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
