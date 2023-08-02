package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.location.Gugun;
import com.ssafy.fcc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GugunRepository {

    public final EntityManager em;

    public Gugun findById(int id) {
        Gugun gugun = em.find(Gugun.class, id);
        return gugun;
    }

    public List<Gugun> findAll(){
        List<Gugun> gugunList = em.createQuery("select a from Gugun a", Gugun.class).getResultList();
        return gugunList;

    }


    public List<Gugun> findGugunBySido(Integer sidoId){
        List<Gugun> gugunList = em.createQuery("select a from Gugun a WHERE a.sido.Id = :sidoId ", Gugun.class)
                .setParameter("sidoId", sidoId)
                .getResultList();

        return gugunList;

    }
}
