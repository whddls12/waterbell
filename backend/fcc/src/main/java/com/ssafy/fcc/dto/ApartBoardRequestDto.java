package com.ssafy.fcc.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApartBoardRequestDto {
    private String title;
    private String content;
    List<MultipartFile> uploadedfiles;
}
