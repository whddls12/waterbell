package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.log.ControlType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseLogDto {

    private LocalDateTime time;
    private String category;
    private String status;
}
