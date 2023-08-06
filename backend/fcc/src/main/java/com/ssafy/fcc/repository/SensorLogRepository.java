package com.ssafy.fcc.repository;


import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.log.SensorLog;
import com.ssafy.fcc.domain.log.SensorType;
import com.ssafy.fcc.dto.SensorLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SensorLogRepository {

    private final EntityManager em;

    public void save(SensorLog sensorLog) {
        em.persist(sensorLog);
    }

    public int getRecentData(Facility facility, SensorType category) {
        return em.createQuery("select a from SensorLog a where a.facility = :facility and a.category = :category order by a.sensorTime DESC ", SensorLog.class )
                .setParameter("facility",facility)
                .setParameter("category", category)
                .setMaxResults(1)
                .getSingleResult()
                .getSensorData();
    }

    public List<SensorLog> getLogList(Facility facility, SensorType category) {

        return em.createQuery("select a from SensorLog a where a.facility = :facility and a.category = :category", SensorLog.class)
                .setParameter("facility", facility)
                .setParameter("category",category)
                .getResultList();
    }
}
