package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.UndergroundRoad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UndergroundRoadRepository {

    private final EntityManager em;

    public UndergroundRoad findById(int facility_id){
        return em.find(UndergroundRoad.class, facility_id);
    }
}
