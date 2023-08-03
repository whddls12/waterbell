package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.PublicManager;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UndergroundRoadBoardService {

    private final PublicManagerRepository publicManagerRepository;
    private final FacilityRepository facilityRepository;
    private final MemberRepository memberRepository;
    private final BoardAlarmLogRepository boardAlarmLogRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final MyWebSocketHandler myWebSocketHandler;

    // 글 작성시 관리자에게 웹 알림
    @Transactional
    public void sendWebNotification(int facility_id, int board_id) throws IOException {
        Facility facility = facilityRepository.findById(facility_id);
        PublicManager manager = publicManagerRepository.findBySido(facility.getGugun().getSido().getId());
        String notificationMessage = "신고접수 게시판에 새로운 접수가 등록되었습니다.";

        // 신고접수 알림로그 저장
        BoardAlarmLog boardAlarmLog = new BoardAlarmLog();
        boardAlarmLog.setMember(memberRepository.getSystemMember());
        boardAlarmLog.setFacility(facility);
        boardAlarmLog.setRegDate(LocalDateTime.now());
        boardAlarmLog.setContent(notificationMessage);
        boardAlarmLog.setIsApart(false);
        boardAlarmLog.setIsFlood(false);
        boardAlarmLog.setApartBoardId(board_id);
        boardAlarmLogRepository.save(boardAlarmLog);

        // 웹 알림 보내고 저장
        ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
        receiveAlarmMember.setAlarm(boardAlarmLog);
        receiveAlarmMember.setMember(manager);
        receiveAlarmMember.setRead(false);
        receiveAlarmMemberRepository.save(receiveAlarmMember);
        myWebSocketHandler.sendNotificationToSpecificUser(manager.getLoginId(), receiveAlarmMember);
    }
}
