package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.log.CommandType;
import com.ssafy.fcc.domain.log.ControlType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ControlLogDto {
    private long id;
    private LocalDateTime time;
    private String name;
    private ControlType category;
    private Integer height;
    private CommandType command;
}
