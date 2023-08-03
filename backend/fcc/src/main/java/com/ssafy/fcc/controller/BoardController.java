package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.BoardStatus;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.dto.ApartBoardRequestDto;
import com.ssafy.fcc.service.ApartBoardService;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class BoardController {

    private final FacilityService facilityService;
    private final MemberService memberService;
    private final ApartBoardService apartBoardService;


    @PostMapping(path  = "/write/apartMember/{facilityId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> writeComment(
            @PathVariable("facilityId") int facilityId,
//            @ModelAttribute(value = "board") ApartBoardRequestDto boardDto,
//            @RequestPart(value = "uploadedfiles", required = false) List<MultipartFile> uploadedfiles
             ApartBoardRequestDto boardDto
    )
            throws IllegalStateException, IOException  {


        final int memberId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Apart apart = (Apart) facilityService.findById(facilityId);
        ApartMember apartMember = (ApartMember) memberService.findById(memberId);

        System.out.println(boardDto);
        ApartBoard apartBoard = new ApartBoard();
        apartBoard.setApart(apart);
        apartBoard.setApartMember(apartMember);
        apartBoard.setTitle(boardDto.getTitle());
        apartBoard.setContent(boardDto.getContent());
        apartBoard.setStatus(BoardStatus.BEFORE);
        apartBoard.setCreateDate(LocalDateTime.now());
        apartBoard.setViewCount(0);

        System.out.println("댓글쓰기: " + apartBoard);
        try {
            final Integer apartBoardId = apartBoardService.writeApartBoard(apartBoard, boardDto.getUploadedfiles());
            //apartBoardService.sendWebNotification(memberId,apartBoardId);
            return new ResponseEntity<String>("success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
        }
    }




}
