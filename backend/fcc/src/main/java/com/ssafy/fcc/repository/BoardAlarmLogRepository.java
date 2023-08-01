package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BoardAlarmLogRepository {

    private final EntityManager em;

    public void save(BoardAlarmLog boardAlarmLog) {
        em.persist(boardAlarmLog);
    }
}
