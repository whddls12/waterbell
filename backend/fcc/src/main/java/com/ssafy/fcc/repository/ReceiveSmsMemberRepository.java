package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.sms.ReceiveSmsMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}
