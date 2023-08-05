package com.ssafy.fcc.repository;


import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.log.SensorLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class SensorLogRepository {

    private final EntityManager em;

    public void save(SensorLog sensorLog) {
        em.persist(sensorLog);
    }

    public int getRecentData(Facility facility, String category) {
        return em.createQuery("select a from SensorLog a where a.facility = :facility and a.category = :category order by a.sensorTime DESC ", SensorLog.class )
                .setParameter("facility",facility)
                .setParameter("category", category)
                .setMaxResults(1)
                .getSingleResult()
                .getSensorData();
    }
}
