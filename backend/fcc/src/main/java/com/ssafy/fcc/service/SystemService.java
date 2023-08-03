package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.SensorLog;
import com.ssafy.fcc.repository.FacilityRepository;
import com.ssafy.fcc.repository.GugunRepository;
import com.ssafy.fcc.repository.SensorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class SystemService {

    public final FacilityRepository facilityRepository;
    public final SensorLogRepository sensorLogRepository;

    public void insertLog(String category, int value) {

        Facility facility = facilityRepository.findById(1);



        SensorLog sensorlog = new SensorLog();
        sensorlog.setFacility(facility);
        sensorlog.setCategory(category);
        sensorlog.setSensorTime(LocalDateTime.now());
        sensorlog.setSensorData(value);


        sensorLogRepository.save(sensorlog);



    }

}