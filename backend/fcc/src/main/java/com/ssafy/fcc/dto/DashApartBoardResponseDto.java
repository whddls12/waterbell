package com.ssafy.fcc.dto;

import com.ssafy.fcc.domain.board.BoardStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashApartBoardResponseDto {
    private Integer id;
    private String title;
    private LocalDateTime createDate;
    private BoardStatus status;

}
