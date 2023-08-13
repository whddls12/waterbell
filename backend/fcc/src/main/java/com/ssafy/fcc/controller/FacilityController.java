package com.ssafy.fcc.controller;

//import com.ssafy.fcc.MQTT.MqttPublisher;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.location.Gugun;
import com.ssafy.fcc.dto.ApartDto;
import com.ssafy.fcc.repository.ApartRepository;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.GugunService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor
public class FacilityController {
    public static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final FacilityService facilityService;
    private final GugunService gugunService;
    private final ApartRepository apartRepository;

    @GetMapping("/roads")
    public ResponseEntity<List<Map<String, Object>>> undergroundRoadList() {
        List<Gugun> gugunList = gugunService.findGugunAll();
        List<Map<String, Object>> response = facilityService.findUndergroundRoadByGugunList(gugunList);
        HttpStatus status = null;

        try {
            if (response == null) {
                status = HttpStatus.NO_CONTENT;
            } else {
                status = HttpStatus.ACCEPTED;
            }
        } catch (Exception e) {
            logger.error("지하차도 목록 조회 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    //memberId가 들어오는 방식 !@AuthenticationPrincipal 이게 맞는가 !
    @GetMapping("publicManager/roads")
    public ResponseEntity<List<Map<String, Object>>> undergroundRoadListByPmanager(){
        int id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Gugun> gugunList = gugunService.findGugunByPmanager(id);

        List<Map<String, Object>> response = facilityService.findUndergroundRoadByGugunList(gugunList);
        HttpStatus status = null;

        try {
            if (response == null) {
                status = HttpStatus.NO_CONTENT;
            } else {
                status = HttpStatus.ACCEPTED;
            }

        } catch (Exception e) {
            logger.error("지하차도 목록 조회 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/{facility_id}/status")
    public WaterStatus getWaterStatus(@PathVariable("facility_id") int facilityId) {
        return facilityService.getStatus(facilityId);

    }

    @GetMapping("{facility_id}/apart")
    public ResponseEntity<Map<String, Object>> getApart(@PathVariable("facility_id") int facility_id){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        Apart apart = apartRepository.findById(facility_id);
        if(apart==null){
            status = HttpStatus.NO_CONTENT;
            resultMap.put("message", "유효하지 않은 facility_id입니다.");
        }
        else {
            status = HttpStatus.ACCEPTED;
            ApartDto dto = new ApartDto(apart);
            resultMap.put("apart", dto);
            resultMap.put("message", "조회 성공");
        }
        return new ResponseEntity<>(resultMap, status);
    }


}

