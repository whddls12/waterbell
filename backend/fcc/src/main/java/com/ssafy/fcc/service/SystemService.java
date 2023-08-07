package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.*;
import com.ssafy.fcc.dto.ControlLogDto;
import com.ssafy.fcc.dto.SensorLogDto;
import com.ssafy.fcc.repository.*;
import com.ssafy.fcc.util.PageNavigation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Integer> getSensorData(int facilityId) {
        Map<String, Integer> map = new HashMap<>();
        Facility facility = facilityRepository.findById(facilityId);

        map.put("Dust", sensorLogRepository.getRecentData(facility, SensorType.DUST));
        map.put("Temperature", sensorLogRepository.getRecentData(facility, SensorType.TEMPERATURE));
        map.put("Humidity", sensorLogRepository.getRecentData(facility, SensorType.HUMIDITY));

        return map;
    }

    public Map<String, Object> getSensorLogList(int facilityId, String category_str, int page) {

        Facility facility = facilityRepository.findById(facilityId);
        boolean isApart = facility.isApart();
        SensorType category = SensorType.valueOf(category_str.toUpperCase());

        Long totalCount = sensorLogRepository.getSensorLogCnt(facility, category);
        PageNavigation pageNavigation = new PageNavigation(page, totalCount);


        List<SensorLog> sensorLogList = sensorLogRepository.getLogList(facility, category, pageNavigation.getStart(), pageNavigation.getSizePerPage());

        String name;
        if (isApart) {
            name = apartRepository.findById(facilityId).getApartName();
        } else {
            name = undergroundRoadRepository.findById(facilityId).getUndergroundRoadName();
        }

        List<SensorLogDto> logDtoList = new ArrayList<>();
        for (SensorLog log : sensorLogList) {
            logDtoList.add(new SensorLogDto(log.getId(), log.getSensorTime(), name, category, log.getSensorData()));
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageNavigation", pageNavigation);
        resultMap.put("list", logDtoList);

        return resultMap;
    }

    public Map<String, Object> getControlLogList(int facilityId, int page) {

        try {
            Facility facility = facilityRepository.findById(facilityId);
            boolean isApart = facility.isApart();

            Long totalCount = controlLogRepository.getControlLogCnt(facility);
            PageNavigation pageNavigation = new PageNavigation(page, totalCount);

            List<ControlLog> logList = controlLogRepository.getLogList(facility, pageNavigation.getStart(), pageNavigation.getSizePerPage());

            String name;
            if (isApart) {
                name = apartRepository.findById(facilityId).getApartName();
            } else {
                name = undergroundRoadRepository.findById(facilityId).getUndergroundRoadName();
            }

            List<ControlLogDto> logDtoList = new ArrayList<>();
            for (ControlLog log : logList) {
                logDtoList.add(new ControlLogDto(log.getControlId(), log.getControlTime(), name, log.getCategory(), log.getWaterHeight(), log.getCommand()));
            }

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("pageNavigation", pageNavigation);
            resultMap.put("list", logDtoList);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Transactional
    public int insertControlLog(int facilityId, String command_str) {

        Facility facility = facilityRepository.findById(facilityId);
        ControlType category = facility.isApart() == true ? ControlType.WATERPLATE : ControlType.BILLBOARD;
        LocalDateTime time = LocalDateTime.now().withNano(0);
        int height = sensorLogRepository.getRecentData(facility, SensorType.HEIGHT);
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
