package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.UndergroundRoad;
import com.ssafy.fcc.domain.location.Gugun;
import com.ssafy.fcc.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public List<Map<String, Object>> findUndergroundRoadByGugunList(List<Gugun> gugunList) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (gugunList != null) {
            for (Gugun gugun : gugunList) {
                List<UndergroundRoad> undergroundRoadList = facilityRepository.findUndergroundRoadByGugunId(gugun.getId());
                if (undergroundRoadList == null) continue;
                Map<String, Object> data = new HashMap<>();
                data.put("gugunId", gugun.getId());
                data.put("gugunName", gugun.getGugunName());
                data.put("sido", gugun.getSido());
                data.put("underroads", undergroundRoadList);
                result.add(data);


            }
        }else{
            return null;
        }


        return result;

    }
}
