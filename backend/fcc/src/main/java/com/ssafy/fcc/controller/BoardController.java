package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.BoardStatus;
import com.ssafy.fcc.domain.board.UndergroundRoadBoard;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.UndergroundRoad;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.dto.ApartBoardRequestDto;
import com.ssafy.fcc.dto.DashUndergroundRoadBoardResponseDto;
import com.ssafy.fcc.dto.UndergroundRoadBoardRequestDto;
import com.ssafy.fcc.service.ApartBoardService;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.MemberService;
import com.ssafy.fcc.service.UndergroundRoadBoardService;
import com.ssafy.fcc.util.PageNavigation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class BoardController {

    private final FacilityService facilityService;
    private final MemberService memberService;
    private final ApartBoardService apartBoardService;
    private final UndergroundRoadBoardService undergroundRoadBoardService;


    @PostMapping(path  = "/write/apartMember/{facilityId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> writeApartBoard(
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
            apartBoardService.sendWebNotification(memberId,apartBoardId);
            return new ResponseEntity<String>("success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(path  = "/write/{facilityId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> writeUndergroundRoadBoard(
            @PathVariable("facilityId") int facilityId,
            UndergroundRoadBoardRequestDto boardDto
    )
            throws IllegalStateException, IOException  {
        System.out.println("facilityId= "+facilityId);
        UndergroundRoad undergroundRoad= (UndergroundRoad) facilityService.findById(facilityId);

        System.out.println(boardDto);

        UndergroundRoadBoard undergroundRoadBoard = new UndergroundRoadBoard();
        undergroundRoadBoard.setUndergroundRoad(undergroundRoad);
        undergroundRoadBoard.setTitle(boardDto.getTitle());
        undergroundRoadBoard.setContent(boardDto.getContent());
        undergroundRoadBoard.setName(boardDto.getName());
        undergroundRoadBoard.setPhone(boardDto.getPhone());
        undergroundRoadBoard.setStatus(BoardStatus.BEFORE);
        undergroundRoadBoard.setCreateDate(LocalDateTime.now());
        undergroundRoadBoard.setViewCount(0);
        undergroundRoadBoard.setBoardPassword(boardDto.getBoardPassword());

        System.out.println("댓글쓰기: " + undergroundRoadBoard);
        try {
            final Integer undergroundRoadBoardId =undergroundRoadBoardService.undergroundRoadBoard(undergroundRoadBoard, boardDto.getUploadedfiles());
            undergroundRoadBoardService.sendWebNotification(facilityId,undergroundRoadBoardId);
            return new ResponseEntity<String>("success", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("fail", HttpStatus.NO_CONTENT);
        }
    }



    //대시보드 지하차도 목록 조회
    @GetMapping("/dash/{facilityId}")
    public ResponseEntity<Map<String, Object>>  dashUndergoundBoardList(
            @PathVariable("facilityId") int facilityId
    ){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            final List<DashUndergroundRoadBoardResponseDto> boadListLatest = undergroundRoadBoardService.getBoadListLatest(facilityId);
            resultMap.put("message", "success");
            resultMap.put("list", boadListLatest);
            status = HttpStatus.ACCEPTED;
        }catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @GetMapping("/undergroudRoad/{facilityId}/{page}")
    public ResponseEntity<Map<String, Object>>  undergroundRoadList(
            @PathVariable("facilityId") int facilityId,
            @PathVariable("page") int page){
        Map<String, Object> resultMap= new HashMap<>();
        HttpStatus status = null;

        try {
            resultMap = undergroundRoadBoardService.getBoadListByPage(facilityId,page);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        }catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    //자하차도 신고접수 상세페이지
    @GetMapping("/undergroundRoad/detail/{boardId}")
    public ResponseEntity<Map<String, Object>>  undergroundRoadList(@PathVariable("boardId") int boardId){

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {

            //신고접수 게시글 내용
            UndergroundRoadBoard board = undergroundRoadBoardService.getBoardById(boardId);
            resultMap.put("board", board);
            //이미지 전달


            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;

        }catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


}
