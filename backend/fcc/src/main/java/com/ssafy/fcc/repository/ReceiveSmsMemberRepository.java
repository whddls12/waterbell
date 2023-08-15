package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.sms.ReceiveSmsMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReceiveSmsMemberRepository {

    private final EntityManager em;

    public void save(ReceiveSmsMember receiveSmsMember) {
        em.persist(receiveSmsMember);
    }

    public List<ReceiveSmsMember> findByMemberId(int member_id) {
        return em.createQuery("select R from ReceiveSmsMember R where R.member.id = :member_id", ReceiveSmsMember.class)
                .setParameter("member_id", member_id)
                .getResultList();
    }

    public Long getReceiveAlarmMemberCnt(Long alarm_id){
        String jpqlQuery = "SELECT COUNT(R) FROM ReceiveSmsMember R WHERE R.smslog.id = :alarm_id";
        TypedQuery<Long> countQuery = em.createQuery(jpqlQuery, Long.class)
                .setParameter("alarm_id", alarm_id);
        return countQuery.getSingleResult();
    }
}
