package com.ssafy.fcc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.FloodAlarmLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmLogDto {

    private Long alarm_id;
    private int sender_id;
    private int facility_id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime regDate;
    String content;
    boolean is_apart;
    boolean is_flood;
    int apart_board_id;
    int underground_board_id;
    private String status;

    public AlarmLogDto(BoardAlarmLog log) {
        this.alarm_id = log.getId();
        this.sender_id = log.getMember().getId();
        this.facility_id = log.getFacility().getId();
        this.regDate = log.getRegDate();
        this.content = log.getContent();
        this.is_apart = log.getIsApart();
        this.is_flood = log.getIsFlood();
        if(this.is_apart) this.apart_board_id = log.getApartBoardId();
        else this.underground_board_id = log.getUndergroundBoardId();
    }

    public AlarmLogDto(FloodAlarmLog log) {
        this.alarm_id = log.getId();
        this.sender_id = log.getMember().getId();
        this.facility_id = log.getFacility().getId();
        this.regDate = log.getRegDate();
        this.content = log.getContent();
        this.is_apart = log.getIsApart();
        this.is_flood = log.getIsFlood();
        this.status = log.getStep().name();
    }
}
