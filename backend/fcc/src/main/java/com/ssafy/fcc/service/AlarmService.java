package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.dto.BoardAlarmDto;
import com.ssafy.fcc.repository.FloodAlarmLogRepository;
import com.ssafy.fcc.repository.ReceiveAlarmMemberRepository;
import com.ssafy.fcc.util.DescriptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService {

    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final FloodAlarmLogRepository floodAlarmLogRepository;
    private final DescriptionUtil descriptionUtil;

    // 내 알림 가져오기
    public List<BoardAlarmDto> getAlarmList(int member_id) {
        List<ReceiveAlarmMember> list = receiveAlarmMemberRepository.findByMemberId(member_id);
        List<BoardAlarmDto> boardDtos= new ArrayList<>();
        for(ReceiveAlarmMember receive : list){
            BoardAlarmDto boardAlarm = new BoardAlarmDto();
            if(receive.getAlarm().getIsFlood()){
                FloodAlarmLog flog = floodAlarmLogRepository.findById(receive.getAlarm().getId());
                boardAlarm.setAlarmType(descriptionUtil.getDescription(flog.getStep().name()));
            }
            else{
                boardAlarm.setAlarmType("신고접수 알림");
            }
            boardAlarm.setId(receive.getId());
            boardAlarm.setContent(receive.getAlarm().getContent());
            boardAlarm.setSender(descriptionUtil.getDescription(receive.getAlarm().getMember().getRole().name()));
            boardAlarm.setRegDate(receive.getAlarm().getRegDate());
            boardAlarm.setStatus(receive.isRead() ? "확인" : "미확인");
            boardDtos.add(boardAlarm);
        }
        boardDtos.sort(Comparator.comparing(BoardAlarmDto::getRegDate).reversed());
        return boardDtos;
    }

    // 알림 상세 조회
    public BoardAlarmDto getAlarm(Long receive_alarm_id){
        BoardAlarmDto boardAlarm = new BoardAlarmDto();
        ReceiveAlarmMember receive = receiveAlarmMemberRepository.findById(receive_alarm_id);
        if(!receive.isRead()){
            receive.setRead(true);
            receive.setReadDate(LocalDateTime.now());
        }
        if(receive.getAlarm().getIsFlood()){
            FloodAlarmLog flog = floodAlarmLogRepository.findById(receive.getAlarm().getId());
            boardAlarm.setAlarmType(descriptionUtil.getDescription(flog.getStep().name()));
        }
        else{
            boardAlarm.setAlarmType("신고접수 알림");
        }
        boardAlarm.setId(receive.getId());
        boardAlarm.setContent(receive.getAlarm().getContent());
        boardAlarm.setSender(descriptionUtil.getDescription(receive.getAlarm().getMember().getRole().name()));
        boardAlarm.setRegDate(receive.getAlarm().getRegDate());
        boardAlarm.setStatus("확인");
        return boardAlarm;
    }

    // 본인의 알림이 맞는지 확인
    public boolean checkUser(int member_id, Long receive_alarm_id){
        ReceiveAlarmMember receive = receiveAlarmMemberRepository.findById(receive_alarm_id);
        return receive.getMember().getId() == member_id;
    }
}
