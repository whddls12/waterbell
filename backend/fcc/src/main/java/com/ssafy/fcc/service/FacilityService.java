package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public Apart findApartByCode(String apartCode) {
        return facilityRepository.findApartByCode(apartCode);
    }

    public Facility findById(Integer id) {
       return  facilityRepository.findById(id);
    }
}
