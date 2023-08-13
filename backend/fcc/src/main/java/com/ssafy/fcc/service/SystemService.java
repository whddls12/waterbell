package com.ssafy.fcc.service;

import com.ssafy.fcc.controller.DashController;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.*;
import com.ssafy.fcc.dto.ControlLogDto;
import com.ssafy.fcc.dto.SensorLogDto;
import com.ssafy.fcc.handler.CamWebSocketHandler;
import com.ssafy.fcc.repository.*;
import com.ssafy.fcc.util.PageNavigation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SystemService {

    private final FacilityRepository facilityRepository;
    private final SensorLogRepository sensorLogRepository;
    private final ApartRepository apartRepository;
    private final UndergroundRoadRepository undergroundRoadRepository;
    private final ControlLogRepository controlLogRepository;

    public final FacilityService facilityService;
    public final UndergroundRoadService undergroundRoadService;
    public final ApartService apartService;
    public CamWebSocketHandler camWebSocketHandler;

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
        map.put("Temperature", sensorLogRepository.getRecentData(facility, SensorType.TEMP));
        map.put("Humidity", sensorLogRepository.getRecentData(facility, SensorType.HUMID));

        return map;
    }

    public Map<String, Object> getSensorLogList(int facilityId, String category_str, int page, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        Facility facility = facilityRepository.findById(facilityId);
        boolean isApart = facility.isApart();
        SensorType category = SensorType.valueOf(category_str.toUpperCase());

        Long totalCount = sensorLogRepository.getSensorLogCnt(facility, category);
        PageNavigation pageNavigation = new PageNavigation(page, totalCount);


        List<SensorLog> sensorLogList = sensorLogRepository.getLogList(facility, category, pageNavigation.getStart(), pageNavigation.getSizePerPage(), searchStartDate, searchEndDate);

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

    public Map<String, Object> getControlLogList(int facilityId, int page, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        try {
            Facility facility = facilityRepository.findById(facilityId);
            boolean isApart = facility.isApart();

            Long totalCount = controlLogRepository.getControlLogCnt(facility);
            PageNavigation pageNavigation = new PageNavigation(page, totalCount);

            List<ControlLog> logList = controlLogRepository.getLogList(facility, pageNavigation.getStart(), pageNavigation.getSizePerPage(), searchStartDate, searchEndDate);

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
        ControlType category = facility.isApart() == true ? ControlType.PLATE : ControlType.BOARD;
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

    public Map<String, Integer> getHeightPerhour(int facilityId) {

        Map<String, Integer> resultMap = new TreeMap<>();

        Facility facility = facilityRepository.findById(facilityId);

//        for (int i = 5; i >= 0; i--) {
//            LocalDateTime time = LocalDateTime.now().minusHours(i);
//            int height = sensorLogRepository.getHeightPerhour(facility, SensorType.HEIGHT, time);
//            resultMap.put(LocalDateTime.now().minusHours(i).getHour(), height);
//        }

        LocalDateTime time = LocalDateTime.now();
        List<SensorLog> sensorLogList = sensorLogRepository.getHeightPerhour(facility,SensorType.HEIGHT,time);
        for(SensorLog log : sensorLogList) {
            resultMap.put(log.getSensorTime().toString().substring(11,16), log.getSensorData());
        }
        return resultMap;
    }

    public void fromSensor(String message) throws Exception {
        String[] result = message.toString().split("/");

        int facilityId = Integer.parseInt(result[0]);
        SensorType sensorType = SensorType.valueOf(result[1]);
        int value = Integer.parseInt(result[2]);

        Facility facility = facilityRepository.findById(facilityId);

        if (sensorType.equals(SensorType.HEIGHT)) {
            checkSituation(facility, value);
        }

        insertLog(facilityId, sensorType, value);

    }

    @Transactional
    public void checkSituation(Facility facility, int value) throws Exception {

        if (value > facility.getSecondAlarmValue()) {
            if (facility.getStatus() == WaterStatus.FIRST || facility.getStatus() == WaterStatus.DEFAULT) {
                facilityService.updateStatus(facility, WaterStatus.SECOND);
                if (facility.isApart()) { // 아파트
                    apartService.sendAutoNotificationToManager(facility.getId(), facility.getStatus(), value);
                } else { // 지하차도
                    undergroundRoadService.sendAutoNotification(facility.getId(), facility.getStatus(), value);
                }
            }

        } else if (value > facility.getFirstAlarmValue()) {
            if (facility.getStatus() == WaterStatus.DEFAULT) {
                facilityService.updateStatus(facility, WaterStatus.FIRST);
                if (facility.isApart()) { // 아파트
                    apartService.sendAutoNotificationToManager(facility.getId(), facility.getStatus(), value);
                    apartService.sendAutoNotificationToMember(facility.getId());
                } else { // 지하차도
                    undergroundRoadService.sendAutoNotification(facility.getId(), facility.getStatus(), value);
                }
            } else if (facility.getStatus() == WaterStatus.SECOND) {
                facilityService.updateStatus(facility, WaterStatus.FIRST);
            }

        } else {
            facilityService.updateStatus(facility, WaterStatus.DEFAULT);
        }

    }

    public int getLatestHeightSensor(int facilityId){
        Facility facility = facilityRepository.findById(facilityId);
        return sensorLogRepository.getRecentData(facility, SensorType.HEIGHT);
    }
}