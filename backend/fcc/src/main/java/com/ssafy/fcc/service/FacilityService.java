package com.ssafy.fcc.service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.ssafy.fcc.config.AwsIoTConfig;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.UndergroundRoad;
import com.ssafy.fcc.domain.location.Gugun;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.dto.FacilityManagementDto;
import com.ssafy.fcc.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        } else {
            return null;
        }


        return result;
    }

    @Transactional
    public void updateStatus(Facility facility, WaterStatus status) throws AWSIotException {

        facility.setStatus(status);
        facilityRepository.merge(facility);


    }

    public WaterStatus getStatus(int facilityId) {
        Facility facility = facilityRepository.findById(facilityId);
        return facility.getStatus();
    }

    private final FacilityRepository facilityRepository;

    public Apart findApartByCode(String apartCode) {
        return facilityRepository.findApartByCode(apartCode);
    }

    public Facility findById(Integer id) {
        return facilityRepository.findById(id);
    }

    @Transactional
    public Integer modifyManagementInfo(Integer facilityId, FacilityManagementDto dto) throws Exception {
        Facility facility = facilityRepository.findById(facilityId);

        //1차 높이
        if (dto.getFirstAlarmValue() == null || dto.getFirstAlarmValue().intValue() == 0) {
            if (facility.isApart()) {
                facility.setFirstAlarmValue(15);
                dto.setFirstAlarmValue(15);
            } else {
                facility.setFirstAlarmValue(10);
                dto.setFirstAlarmValue(10);
            }
        } else
            facility.setFirstAlarmValue(dto.getFirstAlarmValue());

        //2차 높이
        if (dto.getSecondAlarmValue() == null || dto.getSecondAlarmValue().intValue() == 0) {
            if (facility.isApart()) {
                facility.setSecondAlarmValue(30);
                dto.setSecondAlarmValue(30);
            } else {
                facility.setSecondAlarmValue(20);
                dto.setSecondAlarmValue(20);
            }
        } else facility.setSecondAlarmValue(dto.getSecondAlarmValue());

        //1차 침수 알림
        if (dto.getFirstFloodMessage() == null || dto.getFirstFloodMessage().equals("")) {
            if (facility.isApart()) {
                facility.setFirstFloodMessage("[WaterBell]주의: 지하주차장의 수위가 1차 경고 기준치에 도달했습니다. 주차장에 차량을 주차하신 분들은 가능한 빠르게 차량을 지상으로 이동시켜 주시기 바랍니다.");
            } else {
                facility.setFirstFloodMessage("침수경고 현재수위:" + dto.getFirstAlarmValue() + "mm 이상");
            }
        } else facility.setFirstFloodMessage(dto.getFirstFloodMessage());


        //2차 침수 알림
        if (dto.getActivation_message() == null || dto.getActivation_message().equals("")) {
            if (facility.isApart()) {
                facility.setActivation_message("[WaterBell]차수판이 작동되었습니다. 지하 주자장 내 차량을 이동하시던 분들은 정차하시고 주차장 밖으로 나와주시길 바랍니다");
            } else {
                facility.setActivation_message("진입금지");
            }
        } else facility.setActivation_message(dto.getActivation_message());


        //작동해제 알림
        if (dto.getDeactivation_message() == null || dto.getDeactivation_message().equals("")) {
            if (facility.isApart()) {
                facility.setDeactivation_message("[WaterBell]차수판 동작이 해제되었습니다. 차량 이동이 가능하나 주차장이 혼잡할 수 있으니 천천히 출차 바랍니다.");
            } else {
                facility.setDeactivation_message("진입금지 해제");
            }
        } else facility.setDeactivation_message(dto.getDeactivation_message());

        return facilityId;
    }
}
