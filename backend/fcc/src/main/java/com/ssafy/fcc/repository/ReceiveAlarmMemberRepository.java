package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReceiveAlarmMemberRepository {

    private final EntityManager em;

    public void save(ReceiveAlarmMember receiveAlarmMember) {
        em.persist(receiveAlarmMember);
    }
}
