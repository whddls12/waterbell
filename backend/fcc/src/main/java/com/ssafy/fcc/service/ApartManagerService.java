package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApartManagerService {

    private final ApartManagerRepository apartManagerRepository;
    private final ApartMemberRepository apartMemberRepository;
    private final FloodAlarmLogRepository floodAlarmLogRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final SmsLogRepository smsLogRepository;
    private final MyWebSocketHandler myWebSocketHandler;
    private final ReceiveSmsMemberRepository receiveSmsMemberRepository;
    private final SmsUtil smsUtil;

    // 아파트관리자 입주민들 찾기
    public List<ApartMember> getMembersOfManager(int member_id) {
        // 주어진 ID로 ApartManager 찾기
        ApartManager manager = apartManagerRepository.findById(member_id);
        // 찾은 ApartManager의 facility_id 가져오기
        Integer facilityId = manager.getApart().getId();
        // 같은 facility_id를 가진 ApartMember 모두 찾기
        List<ApartMember> members = apartMemberRepository.findByApartId(facilityId);

        return members;
    }

    // 입주민들 차수판 웹알림 보내기
    @Transactional
    public void sendFloodWebNotification(int member_id, Step step) throws IOException {
        // 알림 메세지
        String notificationMessage;
        if(step.name().equals("ACTIVATION")){
            notificationMessage = "차수판 동작 메시지"; // 차수판 동작
        }
        else {
            notificationMessage = "차수판 해제 메시지"; // 차수판 해제
        }
        // 알림 로그 만들고 저장하기
        FloodAlarmLog log = new FloodAlarmLog();
        ApartManager manager = apartManagerRepository.findById(member_id);
        log.setMember(manager);
        log.setFacility(manager.getApart());
        log.setRegDate(LocalDateTime.now());
        log.setContent(notificationMessage);
        log.setIsApart(true);
        log.setIsFlood(true);
        log.setStep(step);
        floodAlarmLogRepository.save(log);

        // 입주민들에게 웹 소켓 알림 보내면서 알림 받은 사람 만들고 저장하기
        List<ApartMember> members = getMembersOfManager(member_id);
        for(ApartMember member : members){
            ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
            receiveAlarmMember.setAlarm(log);
            receiveAlarmMember.setApartMember(member);
            receiveAlarmMember.setRead(false);
            receiveAlarmMemberRepository.save(receiveAlarmMember);
            myWebSocketHandler.sendNotificationToSpecificUser(member.getLoginId(), notificationMessage);
        }

    }

    // 입주민들 차수판 문자 알림 보내기
    @Transactional
    public void sendSmsWebNotification(int member_id, Step step) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        // 알림 메세지
        String notificationMessage;
        if(step.name().equals("ACTIVATION")){
            notificationMessage = "차수판 동작 메시지"; // 차수판 동작
        }
        else {
            notificationMessage = "차수판 해제 메시지"; // 차수판 해제
        }
        // sms 로그 만들고 저장하기
        ApartManager manager = apartManagerRepository.findById(member_id);
        SmsLog smsLog = new SmsLog();
        smsLog.setMember(manager);
        smsLog.setSendDate(LocalDateTime.now());
        smsLog.setContent(notificationMessage);
        smsLogRepository.save(smsLog);

        // 입주민들에게 sms 알림 보내면서 알림 받은 사람 만들고 저장하기
        List<ApartMember> members = getMembersOfManager(member_id);
        for(ApartMember member : members){
            ReceiveSmsMember receiveSmsMember = new ReceiveSmsMember();
            receiveSmsMember.setMember(member);
            receiveSmsMember.setSmslog(smsLog);
            receiveSmsMemberRepository.save(receiveSmsMember);
            smsUtil.sendSMS(manager.getPhone(), member.getPhone(), notificationMessage);
        }
    }

}
