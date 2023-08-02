package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}
