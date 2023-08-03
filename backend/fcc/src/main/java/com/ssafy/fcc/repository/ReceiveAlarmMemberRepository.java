package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReceiveAlarmMemberRepository {

    private final EntityManager em;

    public void save(ReceiveAlarmMember receiveAlarmMember) {
        em.persist(receiveAlarmMember);
    }

    public List<ReceiveAlarmMember> findByMemberId(int member_id){
        return em.createQuery("select R from ReceiveAlarmMember  R where R.member.id = :member_id", ReceiveAlarmMember.class)
                .setParameter("member_id", member_id)
                .getResultList();
    }

    public ReceiveAlarmMember findById(Long id){
        return em.find(ReceiveAlarmMember.class, id);
    }
}
