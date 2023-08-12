package com.ssafy.fcc.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUndergroundRoadBoardRequestDto {
    private String name;
    private String title;
    private String content;
    private int boardPassword;
    private List<Integer> removefiles;
    List<MultipartFile> addUploadedfiles;

}
