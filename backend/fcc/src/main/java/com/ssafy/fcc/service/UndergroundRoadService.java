package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.facility.UndergroundRoad;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.member.PublicManager;
import com.ssafy.fcc.domain.sms.ReceiveSmsMember;
import com.ssafy.fcc.domain.sms.SmsLog;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.repository.*;
import com.ssafy.fcc.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UndergroundRoadService {

    private final PublicManagerRepository publicManagerRepository;
    private final UndergroundRoadRepository undergroundRoadRepository;
    private final FloodAlarmLogRepository floodAlarmLogRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final ReceiveSmsMemberRepository receiveSmsMemberRepository;
    private final MemberRepository memberRepository;
    private final MyWebSocketHandler myWebSocketHandler;
    private final SmsLogRepository smsLogRepository;
    private final SmsUtil smsUtil;

    // 1,2차 자동알림(관리자)
    @Transactional
    public void sendAutoNotification(int facility_id, WaterStatus status, int data) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        UndergroundRoad undergroundRoad = undergroundRoadRepository.findById(facility_id);
        // 해당 지하차도 관리자
        PublicManager manager = publicManagerRepository.findBySido(undergroundRoad.getGugun().getSido().getId());
        // 알림 메시지
        String notificationMessage;
        // 1차 경고 상황
        if(status==WaterStatus.FIRST){
            notificationMessage = "[WaterBell]주의 : " + undergroundRoad.getUndergroundRoadName() + "의 수위 센서가 1차 경고 수치인 "
                    + undergroundRoad.getFirstAlarmValue() +"mm를 넘었습니다. 현재 수위는 " + data + "mm입니다. CCTV를 확인하세요.";
        }
        // 2차 경고 상황
        else {
            notificationMessage = "[WaterBell]주의 : " + undergroundRoad.getUndergroundRoadName() + "의 수위 센서가 2차 경고 수치인 "
                    + undergroundRoad.getSecondAlarmValue() +"mm를 넘었습니다. 현재 수위는 " + data + "mm입니다. CCTV를 확인하고 전광판과 경고등을 작동시키세요.";
        }

        // 알림 로그
        FloodAlarmLog floodAlarmLog = new FloodAlarmLog();
        floodAlarmLog.setMember(memberRepository.getSystemMember());
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
        receiveAlarmMember.setMember(manager);
        receiveAlarmMember.setRead(false);
        receiveAlarmMemberRepository.save(receiveAlarmMember);
        myWebSocketHandler.sendNotificationToSpecificUser(manager.getLoginId(), receiveAlarmMember);

        // 문자 알림
        SmsLog smsLog = new SmsLog();
        smsLog.setMember(memberRepository.getSystemMember());
        smsLog.setSendDate(LocalDateTime.now());
        smsLog.setContent(notificationMessage);
        smsLogRepository.save(smsLog);

        // 문자 보내고 저장
        ReceiveSmsMember receiveSmsMember = new ReceiveSmsMember();
        receiveSmsMember.setMember(manager);
        receiveSmsMember.setSmslog(smsLog);
        receiveSmsMemberRepository.save(receiveSmsMember);
        smsUtil.sendSMS(manager.getPhone(), notificationMessage);
    }

}
