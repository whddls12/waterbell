package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.Apart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ApartRepository {

    private final EntityManager em;

    public Apart findById(int facility_id) {
        return em.find(Apart.class, facility_id);
    }
}
