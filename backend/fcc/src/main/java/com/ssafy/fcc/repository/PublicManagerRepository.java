package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.member.PublicManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
@RequiredArgsConstructor
public class PublicManagerRepository {

    private final EntityManager em;

    public PublicManager findById(int member_id) {
        return em.find(PublicManager.class, member_id);
    }

    public PublicManager findBySido(int sido_id){
        try {
            return em.createQuery("select p from PublicManager  p where p.sido.id = :sido_id", PublicManager.class)
                    .setParameter("sido_id", sido_id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
