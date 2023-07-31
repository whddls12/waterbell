package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.facility.UndergroundRoad;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.member.PublicManager;
import com.ssafy.fcc.repository.FloodAlarmLogRepository;
import com.ssafy.fcc.repository.PublicManagerRepository;
import com.ssafy.fcc.repository.ReceiveAlarmMemberRepository;
import com.ssafy.fcc.repository.UndergroundRoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UndergroundRoadService {

    private final PublicManagerRepository publicManagerRepository;
    private final UndergroundRoadRepository undergroundRoadRepository;
    private final FloodAlarmLogRepository floodAlarmLogRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;

    // 1차, 2차 지하차도 자동알림(관리자)
    @Transactional
    public void sendAutoNotification(int facility_id, WaterStatus status){
        UndergroundRoad undergroundRoad = undergroundRoadRepository.findById(facility_id);
        // 해당 지하차도 관리자
        PublicManager manager = publicManagerRepository.findBySido(undergroundRoad.getGugun().getSido().getId());
        // 알림 메시지
        String notificationMessage;
        // 1차 경고 상황
        if(status.name().equals(WaterStatus.FIRST.name())){
            notificationMessage = undergroundRoad.getFirstFloodMessage();
        }
        // 2차 경고 상황
        else {
            notificationMessage = undergroundRoad.getActivation_message();
        }

        // 알림 로그
        FloodAlarmLog floodAlarmLog = new FloodAlarmLog();
        floodAlarmLog.setMember(manager);
        floodAlarmLog.setFacility(undergroundRoad);
        floodAlarmLog.setRegDate(LocalDateTime.now());
        floodAlarmLog.setContent(notificationMessage);
        floodAlarmLog.setIsApart(false);
        floodAlarmLog.setIsFlood(true);
        floodAlarmLog.setStep(Step.FIRST);
        floodAlarmLogRepository.save(floodAlarmLog);

        // 웹 알림 보내고 저장
        ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
        receiveAlarmMember.setAlarm(floodAlarmLog);
//        receiveAlarmMember.setApartMember(manager);

        // 문자 알림
    }
}
