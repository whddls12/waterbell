package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.location.Gugun;
import com.ssafy.fcc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class GugunRepository {

    public final EntityManager em;

    public Gugun findById(int id) {
        Gugun gugun = em.find(Gugun.class, id);
        return gugun;
    }
}
