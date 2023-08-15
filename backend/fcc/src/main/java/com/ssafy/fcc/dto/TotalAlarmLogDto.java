package com.ssafy.fcc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.sms.SmsLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalAlarmLogDto {

    private Long alarm_id;
    private String sender_name;
    private int facility_id;
    private String facility_name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime regDate;
    private String content;
    private String category;
    private Long cnt;

    public TotalAlarmLogDto(BoardAlarmLog log) {
        this.alarm_id = log.getId();
        this.sender_name = log.getMember().getRole().name();
        this.facility_id = log.getFacility().getId();
        this.regDate = log.getRegDate();
        this.content = log.getContent();
        this.category = "신고접수";
    }

    public TotalAlarmLogDto(FloodAlarmLog log) {
        this.alarm_id = log.getId();
        this.sender_name = log.getMember().getRole().name();
        this.facility_id = log.getFacility().getId();
        this.regDate = log.getRegDate();
        this.content = log.getContent();
        if(log.getStep() == Step.ACTIVATION) this.category = "차수판동작";
        else if (log.getStep() == Step.FIRST) this.category = "1차 경고 알림";
        else this.category = "차수판해제";
    }

    public TotalAlarmLogDto(SmsLog log) {
        this.alarm_id = log.getId();
        this.sender_name = log.getMember().getRole().name();
        this.regDate = log.getSendDate();
        this.content = log.getContent();
        this.category = "SMS알림";
    }
}
