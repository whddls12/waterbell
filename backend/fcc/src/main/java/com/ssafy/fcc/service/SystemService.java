package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.*;
import com.ssafy.fcc.dto.ControlLogDto;
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

    private final FacilityRepository facilityRepository;
    private final SensorLogRepository sensorLogRepository;
    private final ApartRepository apartRepository;
    private final UndergroundRoadRepository undergroundRoadRepository;
    private final ControlLogRepository controlLogRepository;

    public void insertLog(int facilityId, SensorType category, int value) {

        Facility facility = facilityRepository.findById(facilityId);

        SensorLog sensorlog = new SensorLog();
        sensorlog.setFacility(facility);
        sensorlog.setCategory(category);
        sensorlog.setSensorTime(LocalDateTime.now().withNano(0));
        sensorlog.setSensorData(value);

        sensorLogRepository.save(sensorlog);

    }

    public int getSensorData(int facilityId, String category_str) {
        Facility facility = facilityRepository.findById(facilityId);
        SensorType category = SensorType.valueOf(category_str.toUpperCase());
        return sensorLogRepository.getRecentData(facility, category);
    }

    public List<SensorLogDto> getSensorLogList(int facilityId, String category_str) {

        Facility facility = facilityRepository.findById(facilityId);
        boolean isApart = facility.isApart();
        SensorType category = SensorType.valueOf(category_str.toUpperCase());
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

    public List<ControlLogDto> getControlLogList(int facilityId) {

        Facility facility = facilityRepository.findById(facilityId);
        boolean isApart = facility.isApart();

        List<ControlLog> logList = controlLogRepository.getLogList(facility);

        String name;
        if(isApart) {
            name = apartRepository.findById(facilityId).getApartName();
        } else {
            name = undergroundRoadRepository.findById(facilityId).getUndergroundRoadName();
        }

        List<ControlLogDto> logDtoList = new ArrayList<>();
        for(ControlLog log : logList) {
            logDtoList.add(new ControlLogDto(log.getControlId(), log.getControlTime(), name, log.getCategory(),log.getWaterHeight(), log.getCommand()));
        }

        return logDtoList;


    }
    @Transactional
    public int insertControlLog(int facilityId, String command_str) {

        Facility facility = facilityRepository.findById(facilityId);
        ControlType category = facility.isApart() == true ? ControlType.WATERPLATE : ControlType.BILLBOARD;
        LocalDateTime time = LocalDateTime.now().withNano(0);
        int height = getSensorData(facilityId, "height");
        CommandType command = CommandType.valueOf(command_str.toUpperCase());

        ControlLog controlLog = new ControlLog();
        controlLog.setFacility(facility);
        controlLog.setCategory(category);
        controlLog.setControlTime(time);
        controlLog.setWaterHeight(height);
        controlLog.setCommand(command);

        controlLogRepository.save(controlLog);

        return 1;
    }


}
