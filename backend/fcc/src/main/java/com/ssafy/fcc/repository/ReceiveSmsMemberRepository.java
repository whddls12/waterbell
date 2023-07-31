package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.sms.ReceiveSmsMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ReceiveSmsMemberRepository {

    private final EntityManager em;

    public void save(ReceiveSmsMember receiveSmsMember) {
        em.persist(receiveSmsMember);
    }
}
