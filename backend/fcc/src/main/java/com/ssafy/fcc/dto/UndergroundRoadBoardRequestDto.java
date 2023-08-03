package com.ssafy.fcc.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UndergroundRoadBoardRequestDto {
    private String name;
    private String phone;
    private String title;
    private String content;
    private int boardPassword;
    List<MultipartFile> uploadedfiles;
}
