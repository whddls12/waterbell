package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReceiveAlarmMemberRepository {

    private final EntityManager em;

    public void save(ReceiveAlarmMember receiveAlarmMember) {
        em.persist(receiveAlarmMember);
    }

    public List<ReceiveAlarmMember> findByMemberId(int member_id, int start, int size){
        String latestQueery = "SELECT R FROM ReceiveAlarmMember R WHERE R.member.id = :member_id ORDER BY R.alarm.regDate DESC, R.id DESC";
        List<ReceiveAlarmMember> resultList = em.createQuery(latestQueery, ReceiveAlarmMember.class)
                .setParameter("member_id", member_id)
                .setFirstResult(start)
                .setMaxResults(size)
                .getResultList();
        return resultList;
    }

    public Long getReceiveAlarmMemberCnt(int member_id){
        String jpqlQuery = "SELECT COUNT(R) FROM ReceiveAlarmMember R WHERE R.member.id = :member_id";
        TypedQuery<Long> countQuery = em.createQuery(jpqlQuery, Long.class)
                .setParameter("member_id", member_id);
        return countQuery.getSingleResult();
    }

    public Long getReceiveAlarmMemberCnt(Long alarm_id){
        String jpqlQuery = "SELECT COUNT(R) FROM ReceiveAlarmMember R WHERE R.alarm.id = :alarm_id";
        TypedQuery<Long> countQuery = em.createQuery(jpqlQuery, Long.class)
                .setParameter("alarm_id", alarm_id);
        return countQuery.getSingleResult();
    }

    public ReceiveAlarmMember findById(Long id){
        return em.find(ReceiveAlarmMember.class, id);
    }
}
