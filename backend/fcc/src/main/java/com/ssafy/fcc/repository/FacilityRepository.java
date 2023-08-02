package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FacilityRepository {

    private final EntityManager em;

    public Apart findApartByCode(String apartCode) {
        List<Apart> apartList = em.createQuery("select a from Apart a WHERE a.apartCode = :apartCode ", Apart.class)
                .setParameter("apartCode", apartCode)
                .getResultList();
        if(apartList.size()>0) return apartList.get(0);
        else return null;
    }

    public Facility findById(Integer id) {
        return em.find(Facility.class, id);
    }

    public WaterStatus findStatusById(Integer id) {
        return em.createQuery("select f from Facility f where f.id = :id", Facility.class)
                .setParameter("id",id)
                .getSingleResult().getStatus();
    }
}
