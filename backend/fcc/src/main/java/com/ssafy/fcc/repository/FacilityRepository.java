package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.location.Sido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FacilityRepository {

    private final EntityManager em;

    public Apart findApartByCode(String apartCode) {

        try {
            return em.createQuery("select a from Apart a WHERE a.apartCode = :apartCode ", Apart.class)
                    .setParameter("apartCode", apartCode)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public Facility findById(Integer id) {
        return em.find(Facility.class, id);
    }


}
