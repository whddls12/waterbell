package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApartManagerRepository {

    private final EntityManager em;

    public ApartManager findById(int member_id){
        return em.find(ApartManager.class, member_id);
    }

    public ApartManager findByFacility(int facility_id){
        return em.createQuery("select m from  ApartManager m where m.apart.id = :apartId", ApartManager.class)
                .setParameter("apartId", facility_id)
                .getSingleResult();
    }

    public List<ApartMember> findMembersByManagerId(int member_id) {
        return em.createQuery("select am from ApartMember am where am.apart = (select a from ApartManager amg join amg.apart a where amg.id = :managerId)", ApartMember.class)
                .setParameter("managerId", member_id)
                .getResultList();
    }

}
