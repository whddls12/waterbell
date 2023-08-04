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
import com.ssafy.fcc.dto.BoardAlarmDto;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.repository.*;
import com.ssafy.fcc.util.DescriptionUtil;
import com.ssafy.fcc.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApartManagerService {

    private final ApartManagerRepository apartManagerRepository;
    private final FloodAlarmLogRepository floodAlarmLogRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final SmsLogRepository smsLogRepository;
    private final MyWebSocketHandler myWebSocketHandler;
    private final ReceiveSmsMemberRepository receiveSmsMemberRepository;
    private final SmsUtil smsUtil;

    // 입주민들 차수판 웹알림 보내기
    @Transactional
    public void sendFloodWebNotification(int member_id, Step step) throws IOException {
        ApartManager manager = apartManagerRepository.findById(member_id);
        Apart apart = manager.getApart();
        // 알림 메세지
        String notificationMessage;
        if(step==Step.ACTIVATION){
            notificationMessage = apart.getActivation_message();
            apart.setStatus(WaterStatus.WORKING);
        }
        else {
            notificationMessage = apart.getDeactivation_message();
            apart.setStatus(WaterStatus.SECOND);
        }
        // 알림 로그
        FloodAlarmLog log = new FloodAlarmLog();
        log.setMember(manager);
        log.setFacility(manager.getApart());
        log.setRegDate(LocalDateTime.now());
        log.setContent(notificationMessage);
        log.setIsApart(true);
        log.setIsFlood(true);
        log.setStep(step);
        floodAlarmLogRepository.save(log);

        // 입주민들에게 웹 알림 보내고 저장하기(로그인 상태라면 소켓 실시간 알림)
        List<ApartMember> members = apartManagerRepository.findMembersByManagerId(member_id);
        for(ApartMember member : members){
            ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
            receiveAlarmMember.setAlarm(log);
            receiveAlarmMember.setMember(member);
            receiveAlarmMember.setRead(false);
            receiveAlarmMemberRepository.save(receiveAlarmMember);
            myWebSocketHandler.sendNotificationToSpecificUser(member.getLoginId(), receiveAlarmMember);
        }

    }

    // 입주민들 차수판 문자 알림 보내기
    @Transactional
    public void sendSmsWebNotification(int member_id, Step step) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        ApartManager manager = apartManagerRepository.findById(member_id);
        Apart apart = manager.getApart();
        // 알림 메세지
        String notificationMessage;
        if(step==Step.ACTIVATION){
            notificationMessage = apart.getActivation_message();
        }
        else {
            notificationMessage = apart.getDeactivation_message();
        }
        // sms 로그
        SmsLog smsLog = new SmsLog();
        smsLog.setMember(manager);
        smsLog.setSendDate(LocalDateTime.now());
        smsLog.setContent(notificationMessage);
        smsLogRepository.save(smsLog);

        // 입주민들에게 sms 알림 보내고 저장
        List<ApartMember> members = apartManagerRepository.findMembersByManagerId(member_id);
        for(ApartMember member : members){
            ReceiveSmsMember receiveSmsMember = new ReceiveSmsMember();
            receiveSmsMember.setMember(member);
            receiveSmsMember.setSmslog(smsLog);
            receiveSmsMemberRepository.save(receiveSmsMember);
            smsUtil.sendSMS(member.getPhone(), notificationMessage);
        }
    }

}
