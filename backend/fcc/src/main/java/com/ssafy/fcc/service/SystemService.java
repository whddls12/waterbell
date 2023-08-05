package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.SensorLog;
import com.ssafy.fcc.dto.SensorLogDto;
import com.ssafy.fcc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SystemService {

    public final FacilityRepository facilityRepository;
    public final SensorLogRepository sensorLogRepository;
    public final ApartRepository apartRepository;
    public final UndergroundRoadRepository undergroundRoadRepository;

    public void insertLog(int facilityId, String category, int value) {

        Facility facility = facilityRepository.findById(facilityId);

        SensorLog sensorlog = new SensorLog();
        sensorlog.setFacility(facility);
        sensorlog.setCategory(category);
        sensorlog.setSensorTime(LocalDateTime.now().withNano(0));
        sensorlog.setSensorData(value);

        sensorLogRepository.save(sensorlog);

    }

    public int getSensorData(int facilityId, String category) {
        Facility facility = facilityRepository.findById(facilityId);

        return sensorLogRepository.getRecentData(facility, category);
    }

    public List<SensorLogDto> getList(int facilityId, String category) {

        Facility facility = facilityRepository.findById(facilityId);
        boolean isApart = facility.isApart();
        List<SensorLog> sensorLogList = sensorLogRepository.getLogList(facility, category);

        String name;
        if(isApart) {
            name = apartRepository.findById(facilityId).getApartName();
        } else {
            name = undergroundRoadRepository.findById(facilityId).getUndergroundRoadName();
        }

        List<SensorLogDto> logDtoList = new ArrayList<>();
        for (SensorLog log : sensorLogList) {
            logDtoList.add(new SensorLogDto(log.getId(), log.getSensorTime(), name, category, log.getSensorData()));
        }

        return logDtoList;
    }
}
