package com.ssafy.fcc.service;


import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.*;
import com.ssafy.fcc.dto.ControlLogDto;
import com.ssafy.fcc.dto.ResponseLogDto;
import com.ssafy.fcc.dto.SensorLogDto;
import com.ssafy.fcc.repository.*;
import com.ssafy.fcc.util.PageNavigation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SystemService {

    private final FacilityRepository facilityRepository;
    private final SensorLogRepository sensorLogRepository;
    private final ResponseLogRepository responseLogRepository;
    private final ApartRepository apartRepository;
    private final UndergroundRoadRepository undergroundRoadRepository;
    private final ControlLogRepository controlLogRepository;

    public final FacilityService facilityService;
    public final UndergroundRoadService undergroundRoadService;
    public final ApartService apartService;


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

        Long totalCount = sensorLogRepository.getSensorLogCnt(facility, category, searchStartDate, searchEndDate);
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

            Long totalCount = controlLogRepository.getControlLogCnt(facility, searchStartDate, searchEndDate);
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

    public Map<String, Object> getResponseLogList(int facilityId, int page, LocalDateTime searchStartDate, LocalDateTime searchEndDate) {

        Facility facility = facilityRepository.findById(facilityId);

        long responseCnt = responseLogRepository.getResponseLogCnt(facility, searchStartDate, searchEndDate);
        PageNavigation pageNavigation = new PageNavigation(page,responseCnt);

        List<ResponseLog> logList = responseLogRepository.getLogList(facility,pageNavigation.getStart(), pageNavigation.getSizePerPage(), searchStartDate, searchEndDate);

        boolean isApart = facility.isApart();
        String name;
        if (isApart) {
            name = apartRepository.findById(facilityId).getApartName();
        } else {
            name = undergroundRoadRepository.findById(facilityId).getUndergroundRoadName();
        }

        List<ResponseLogDto> logDtoList = new ArrayList<>();
        for(ResponseLog log : logList) {
            logDtoList.add(new ResponseLogDto(log.getResponseTime(), isApart == true ? "차수판" : "전광판", log.isStatus() == true ? "정상" : "결함"));
        }

        Map<String,Object> resultMap = new LinkedHashMap<>();
        resultMap.put("pageNavigation",pageNavigation);
        resultMap.put("facilityName",name);
        resultMap.put("list",logDtoList);

        return resultMap;
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

        LocalDateTime time = LocalDateTime.now();
        List<SensorLog> sensorLogList = sensorLogRepository.getHeightPerhour(facility,SensorType.HEIGHT,time);
        for(SensorLog log : sensorLogList) {
            resultMap.put(log.getSensorTime().toString().substring(11,16), log.getSensorData());
        }
        return resultMap;
    }

    public WaterStatus fromSensor(String message) throws Exception {
        String[] result = message.toString().split("/");

        int facilityId = Integer.parseInt(result[0]);
        SensorType sensorType = SensorType.valueOf(result[1]);
        int value = Integer.parseInt(result[2]);

        Facility facility = facilityRepository.findById(facilityId);

        WaterStatus status = null;
        if (sensorType.equals(SensorType.HEIGHT)) {
            status = checkSituation(facility, value);
        }

        insertLog(facilityId, sensorType, value);

        return status;
    }

    @Transactional
    public WaterStatus checkSituation(Facility facility, int value) throws Exception {
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

        return facility.getStatus();
    }

    public int getLatestHeightSensor(int facilityId){
        Facility facility = facilityRepository.findById(facilityId);
        return sensorLogRepository.getRecentData(facility, SensorType.HEIGHT);
    }

    public void checkControl(String message) {

        String[] result = message.split("/");

        int facilityId = Integer.parseInt(result[0]);
        String controlStatus = result[1];

        Facility facility = facilityRepository.findById(facilityId);

        ResponseLog responseLog = new ResponseLog();
        responseLog.setFacility(facility);
        responseLog.setCategory(facility.isApart() == true ? ControlType.BOARD : ControlType.PLATE);
        responseLog.setResponseTime(LocalDateTime.now());
        if(controlStatus.equals("0")) { // 기기가 보낸 상태가 해제 상태일때
            if(facility.getStatus() != WaterStatus.WORKING) {
                responseLog.setStatus(true);
            } else {
                responseLog.setStatus(false);
            }
        } else { // 기기가 보낸 상태가 작동 상태일때
            if(facility.getStatus() == WaterStatus.WORKING) {
                responseLog.setStatus(true);
            } else {
                responseLog.setStatus(false);
            }
        }

        responseLogRepository.save(responseLog);

    }


}