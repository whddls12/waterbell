package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
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
public class ApartBoardService {

    private final ApartMemberRepository apartMemberRepository;
    private final ApartManagerRepository apartManagerRepository;
    private final MemberRepository memberRepository;
    private final BoardAlarmLogRepository boardAlarmLogRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final MyWebSocketHandler myWebSocketHandler;

    // 글 작성시 관리자에게 웹 알림
    @Transactional
    public void sendWebNotification(int member_id, int board_id) throws IOException {
        ApartMember member = apartMemberRepository.findById(member_id);
        ApartManager manager = apartManagerRepository.findByFacility(member.getApart().getId());
        String notificationMessage = "신고접수 게시판에 " + member.getName()+ "님의 접수가 새로 등록되었습니다.";

        // 신고접수 알림로그 저장
        BoardAlarmLog boardAlarmLog = new BoardAlarmLog();
        boardAlarmLog.setMember(memberRepository.getSystemMember());
        boardAlarmLog.setFacility(member.getApart());
        boardAlarmLog.setRegDate(LocalDateTime.now());
        boardAlarmLog.setContent(notificationMessage);
        boardAlarmLog.setIsApart(true);
        boardAlarmLog.setIsFlood(false);
        boardAlarmLog.setApartBoardId(board_id);
        boardAlarmLogRepository.save(boardAlarmLog);

        // 웹 알림 보내고 저장
        ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
        receiveAlarmMember.setAlarm(boardAlarmLog);
        receiveAlarmMember.setMember(manager);
        receiveAlarmMember.setRead(false);
        receiveAlarmMemberRepository.save(receiveAlarmMember);
        myWebSocketHandler.sendNotificationToSpecificUser(manager.getLoginId(), notificationMessage);
    }
}
