package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.sms.ReceiveSmsMember;
import com.ssafy.fcc.domain.sms.SmsLog;
import com.ssafy.fcc.dto.AlarmLogDto;
import com.ssafy.fcc.dto.BoardAlarmDto;
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
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApartService {

    private final ApartManagerRepository apartManagerRepository;
    private final FloodAlarmLogRepository floodAlarmLogRepository;
    private final ApartRepository apartRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final ReceiveSmsMemberRepository receiveSmsMemberRepository;
    private final MemberRepository memberRepository;
    private final MyWebSocketHandler myWebSocketHandler;
    private final SmsLogRepository smsLogRepository;
    private final SmsUtil smsUtil;

    // 1차, 2차 자동알림(관리자)
    @Transactional
    public void sendAutoNotificationToManager(int facility_id, WaterStatus status, int data) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Apart apart = apartRepository.findById(facility_id);
        ApartManager manager = apartManagerRepository.findByFacility(facility_id);
        // 알림 메시지
        String notificationMessage;
        // 알림 로그
        FloodAlarmLog floodAlarmLog = new FloodAlarmLog();
        // 1차 경고 상황
        if(status==WaterStatus.FIRST){
            notificationMessage = "[WaterBell]주의 : " + apart.getApartName() + "의 수위 센서가 1차 경고 수치인 "
                    + apart.getFirstAlarmValue() +"mm를 넘었습니다. 현재 수위는 " + data + "mm입니다. CCTV를 확인하세요.";
            floodAlarmLog.setStep(Step.FIRST);
        }
        // 2차 경고 상황
        else {
            notificationMessage = "[WaterBell]주의 : " + apart.getApartName() + "의 수위 센서가 2차 경고 수치인 "
                    + apart.getSecondAlarmValue() +"mm를 넘었습니다. 현재 수위는 " + data + "mm입니다. CCTV를 확인하고 차수판과 사이렌을 작동시키세요.";
            floodAlarmLog.setStep(Step.SECOND);
        }

        floodAlarmLog.setMember(memberRepository.getSystemMember());
        floodAlarmLog.setFacility(apart);
        floodAlarmLog.setRegDate(LocalDateTime.now());
        floodAlarmLog.setContent(notificationMessage);
        floodAlarmLog.setIsApart(true);
        floodAlarmLog.setIsFlood(true);
        floodAlarmLogRepository.save(floodAlarmLog);
        AlarmLogDto alarmLogDto = new AlarmLogDto(floodAlarmLog);

        // 웹 알림 보내고 저장
        ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
        receiveAlarmMember.setAlarm(floodAlarmLog);
        receiveAlarmMember.setMember(manager);
        receiveAlarmMember.setRead(false);
        receiveAlarmMemberRepository.save(receiveAlarmMember);
        myWebSocketHandler.sendNotificationToSpecificUser(manager.getLoginId(), alarmLogDto);

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

    // 1차 자동알림(입주민)
    @Transactional
    public void sendAutoNotificationToMember(int facility_id) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        Apart apart = apartRepository.findById(facility_id);
        ApartManager manager = apart.getApartManager();
        // 알림 메세지
        String notificationMessage = apart.getFirstFloodMessage();

        // 웹 알림 로그
        FloodAlarmLog log = new FloodAlarmLog();
        log.setMember(manager);
        log.setFacility(manager.getApart());
        log.setRegDate(LocalDateTime.now());
        log.setContent(notificationMessage);
        log.setIsApart(true);
        log.setIsFlood(true);
        log.setStep(Step.FIRST);
        floodAlarmLogRepository.save(log);
        AlarmLogDto alarmLogDto = new AlarmLogDto(log);

        // 입주민들에게 웹 알림 보내고 저장하기(로그인 상태라면 소켓 실시간 알림)
        List<ApartMember> members = apartManagerRepository.findMembersByManagerId(manager.getId());
        for(ApartMember member : members){
            ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
            receiveAlarmMember.setAlarm(log);
            receiveAlarmMember.setMember(member);
            receiveAlarmMember.setRead(false);
            receiveAlarmMemberRepository.save(receiveAlarmMember);
            myWebSocketHandler.sendNotificationToSpecificUser(member.getLoginId(), alarmLogDto);
        }

        // sms 로그
        SmsLog smsLog = new SmsLog();
        smsLog.setMember(manager);
        smsLog.setSendDate(LocalDateTime.now());
        smsLog.setContent(notificationMessage);
        smsLogRepository.save(smsLog);

        // 입주민들에게 sms 알림 보내고 저장
        for(ApartMember member : members){
            ReceiveSmsMember receiveSmsMember = new ReceiveSmsMember();
            receiveSmsMember.setMember(member);
            receiveSmsMember.setSmslog(smsLog);
            receiveSmsMemberRepository.save(receiveSmsMember);
            smsUtil.sendSMS(member.getPhone(), notificationMessage);
        }
    }
}
