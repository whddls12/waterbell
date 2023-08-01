package com.ssafy.fcc.repository;


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
}
