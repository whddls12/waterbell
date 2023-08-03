package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.UndergroundRoad;
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


    public List<UndergroundRoad> findUndergroundRoadByGugunId(Integer gugunId){
        List<UndergroundRoad> undergroundRoadList = em.createQuery("SELECT a FROM UndergroundRoad a WHERE a.gugun.id = :gugunId ", UndergroundRoad.class)
                .setParameter("gugunId", gugunId)
                .getResultList();

        if(undergroundRoadList.size()>0) return undergroundRoadList;
        else return null;
    }

    public void merge(Facility facility) {
        em.merge(facility);
    }

}
