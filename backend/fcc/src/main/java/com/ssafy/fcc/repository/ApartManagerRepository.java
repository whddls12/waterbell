package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.member.ApartManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ApartManagerRepository {

    private final EntityManager em;

    public ApartManager findById(int member_id){
        return em.find(ApartManager.class, member_id);
    }
}
