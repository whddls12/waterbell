package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.BoardStatus;
import com.ssafy.fcc.domain.board.Image;
import com.ssafy.fcc.domain.board.UndergroundRoadBoard;
import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.UndergroundRoad;
import com.ssafy.fcc.domain.member.*;
import com.ssafy.fcc.dto.*;
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

import java.io.EOFException;
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



    //지하차도 신고접수 목록
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



    //지하차도 글쓰기 진입
    @GetMapping("/writeMember")
    public ResponseEntity<Map<String, Object>> writeMemberInfo(){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        if (id == null || id.equals("") || id.equals("anonymousUser")) {
            resultMap.put("message", "비회원");
        } else {
            Member findMember = memberService.findById(Integer.parseInt(id));
            resultMap.put("message", "회원");
            resultMap.put("phone", findMember.getPhone());

            if (findMember.getRole() == Role.APART_MEMBER) {
                resultMap.put("name", ((ApartMember)findMember).getName());
            }
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }



    //지차하도 글쓰기
    @PostMapping(path  = "/write/{facilityId}", consumes = {"multipart/form-data"})
    public  ResponseEntity<Map<String, Object>> writeUndergroundRoadBoard(
            @PathVariable("facilityId") int facilityId,
            UndergroundRoadBoardRequestDto boardDto
    ) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

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
            resultMap.put("message", "success");
            resultMap.put("id", undergroundRoadBoardId);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
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
            undergroundRoadBoardService.updateViewCount(boardId);
            resultMap.put("board", board);
            //이미지 전달
            List<Image> imageList = undergroundRoadBoardService.getImageByBoardId(boardId);
            resultMap.put("imageList", imageList);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;

        }catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    //지하차도 글 수정 - 비밀번호 확인
    @PostMapping("/undergroundRoad/board/password/validation/{boardId}")
    public ResponseEntity<Map<String, Object>>  passwordValidation(@PathVariable("boardId") int boardId, @RequestBody Map<String, Integer> request){

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println(request);

        int boardPassword = request.get("boardPassword").intValue();
        try {
            UndergroundRoadBoard board = undergroundRoadBoardService.getBoardById(boardId);

            if(board.getBoardPassword() == boardPassword){
                resultMap.put("message", "success");
                status = HttpStatus.ACCEPTED;
            }else{
                throw new Exception("게시글 비밀번호가 일치하지 않습니다");
            }

        }catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    //지하차도 글 수정
    @PostMapping(path  = "/update/{undergroundRoadBoardId}", consumes = {"multipart/form-data"})
    public  ResponseEntity<Map<String, Object>> updateUndergroundRoadBoard(
            @PathVariable("undergroundRoadBoardId") int boardId, UpdateUndergroundRoadBoardRequestDto boardDto
    ) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        System.out.println("boardId= "+boardId);
        System.out.println(boardDto);

        try {

            final UndergroundRoadBoard findBoard = undergroundRoadBoardService.getBoardById(boardId);
            if(findBoard.getStatus() != BoardStatus.BEFORE){
                throw new Exception("처리 전 상태에서만 변경 가능");
            }

            undergroundRoadBoardService.updateBoard(boardId, boardDto);

//            undergroundRoadBoardService.sendWebNotification(facilityId,undergroundRoadBoardId);
            resultMap.put("message", "success");
            resultMap.put("id", boardId);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    //지하차도 글 상태 수정 - 관리인
    @GetMapping("/publicManager/updateStatus/{undergroundRoadBoardId}/{boardStatus}")
    public  ResponseEntity<Map<String, Object>> updateStatusUndergroundRoadBoard(
            @PathVariable("undergroundRoadBoardId") int boardId, @PathVariable("boardStatus") String boardStatus) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        System.out.println("boardId= "+boardId);

        try {
            Integer loginId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            Member loginUser = memberService.findById(loginId);
            
            final UndergroundRoadBoard findBoard = undergroundRoadBoardService.getBoardById(boardId);

            if(findBoard.getUndergroundRoad().getGugun().getSido() !=  ((PublicManager)loginUser).getSido()){
                throw new Exception("권한이 없습니다, 담당 지역이 아닙니다");
            }
            undergroundRoadBoardService.updateBoardStatus(boardId, boardStatus);
            
            resultMap.put("message", "success");
            resultMap.put("id", boardId);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    //지하차도 글 삭제 - 관리자
    @GetMapping("/publicManager/deleteBoard/{undergroundRoadBoardId}")
    public  ResponseEntity<Map<String, Object>> deleteUndergroundRoadBoardByManager(
            @PathVariable("undergroundRoadBoardId") int boardId) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println("boardId= "+boardId);

        try {
            Integer loginId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            Member loginUser = memberService.findById(loginId);

            final UndergroundRoadBoard findBoard = undergroundRoadBoardService.getBoardById(boardId);

            if(findBoard.getUndergroundRoad().getGugun().getSido() !=  ((PublicManager)loginUser).getSido()){
                throw new Exception("권한이 없습니다, 담당 지역이 아닙니다");
            }
            undergroundRoadBoardService.deleteBoard(boardId);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    //지하차도 글 삭제 - 글쓴이
    @GetMapping("/deleteBoard/{undergroundRoadBoardId}")
    public  ResponseEntity<Map<String, Object>> deleteUndergroundRoadBoard(
            @PathVariable("undergroundRoadBoardId") int boardId) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        System.out.println("boardId= "+boardId);
//        int boardPassword = request.get("boardPassword").intValue();

        try {
            final UndergroundRoadBoard findBoard = undergroundRoadBoardService.getBoardById(boardId);

//            if(findBoard.getBoardPassword() != boardPassword){
//                throw new Exception("비밀번호가 달라 삭제할 수 없습니다.");
//            }
            undergroundRoadBoardService.deleteBoard(boardId);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }



    //////////////////////////////////////////////////////////////////////////////////////
    //################################여기부터 아파트##################################
    //////////////////////////////////////////////////////////////////////////////////////

    //아파트 대쉬 화면
    @GetMapping("/dash/apart/{facilityId}")
    public ResponseEntity<Map<String, Object>> dashApartList(
            @PathVariable("facilityId") int facilityId
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        Integer loginId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Member loginUser = memberService.findById(loginId);


        try {
            if (loginUser.getRole() == Role.APART_MANAGER) {
                if (((ApartManager) loginUser).getApart().getId() != facilityId) throw new Exception("권한이 없습니다");
            } else if (loginUser.getRole() == Role.APART_MEMBER) {
                if (((ApartMember) loginUser).getApart().getId() != facilityId) throw new Exception("권한이 없습니다");
            } else {
                throw new Exception("권한이 없습니다");
            }

            List<DashApartBoardResponseDto> boadListLatest = apartBoardService.getBoadListLatest(facilityId);
            resultMap.put("message", "success");
            resultMap.put("list", boadListLatest);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }



    //아파트 글쓰기
    @PostMapping(path  = "/write/apartMember/{facilityId}", consumes = {"multipart/form-data"})
    public  ResponseEntity<Map<String, Object>> writeApartBoard(
            @PathVariable("facilityId") int facilityId, ApartBoardRequestDto boardDto) throws IllegalStateException, IOException  {


        Map<String, Object> resultMap= new HashMap<>();
        HttpStatus status = null;

        int memberId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
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

        System.out.println("글쓰기: " + apartBoard);
        try {
            final Integer apartBoardId = apartBoardService.writeApartBoard(apartBoard, boardDto.getUploadedfiles());
            apartBoardService.sendWebNotification(memberId,apartBoardId);
            resultMap.put("message", "success");
            resultMap.put("id", apartBoardId);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    //아파트 신고접수 목록 - 페이징 처리
    @GetMapping("/apartBoard/apart/{facilityId}/{page}")
    public ResponseEntity<Map<String, Object>>  aparatBoardList( @PathVariable("facilityId") int facilityId, @PathVariable("page") int page){

        Map<String, Object> resultMap= new HashMap<>();
        HttpStatus status = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();


        try {
            if (id == null || id.equals("") || id.equals("anonymousUser")) {
                throw new Exception("토큰 필요");
            }

            Member loginUser = memberService.findById(Integer.parseInt(id));
            if (loginUser.getRole() == Role.APART_MANAGER) {
                if (((ApartManager) loginUser).getApart().getId() != facilityId) throw new Exception("권한이 없습니다");
            } else if (loginUser.getRole() == Role.APART_MEMBER) {
                if (((ApartMember) loginUser).getApart().getId() != facilityId) throw new Exception("권한이 없습니다");
            } else {
                throw new Exception("권한이 없습니다");
            }
            resultMap = apartBoardService.getBoadListByPage(facilityId,page);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        }catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    //아파트 신고 접수 상세
    @GetMapping("/apart/ApartBoard/detail/{boardId}")
    public ResponseEntity<Map<String, Object>>  apartBoardList(@PathVariable("boardId") int boardId){

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        try {

            ApartBoard board = apartBoardService.getBoardById(boardId);
            Member loginUser = memberService.findById(Integer.parseInt(id));

            if (loginUser.getRole() == Role.APART_MANAGER) {
                if (((ApartManager) loginUser).getApart().getId() != board.getApart().getId()) throw new Exception("권한이 없습니다");
            } else if (loginUser.getRole() == Role.APART_MEMBER) {
                if (((ApartMember) loginUser).getApart().getId() !=  board.getApart().getId()) throw new Exception("권한이 없습니다");
            } else {
                throw new Exception("권한이 없습니다");
            }
            //여기서 조회수 수정 필요!;
            apartBoardService.updateViewCount(boardId);

            //신고접수 게시글 내용
            resultMap.put("board", board);
            //이미지 전달
            List<Image> imageList = apartBoardService.getImageByBoardId(boardId);
            resultMap.put("imageList", imageList);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;

        }catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    //아파트 신고접수 수정 - 작성자
    @PostMapping(path  = "/apartMember/update/{apartBoardId}", consumes = {"multipart/form-data"})
    public  ResponseEntity<Map<String, Object>> updateApartBoard(
            @PathVariable("apartBoardId") int boardId, UpdateApartBoardRequestDto boardDto
    ) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        System.out.println("boardId= "+boardId);
        System.out.println(boardDto);


        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer loginId = Integer.parseInt(id);

        try {
            ApartBoard findBoard = apartBoardService.getBoardById(boardId);

            ApartMember apartMember = (ApartMember) memberService.findById(loginId);
            if(apartMember.getId() != findBoard.getApartMember().getId()){
                throw new Exception("수정할 수 있는 권한이 없습니다.");
            }

            if(findBoard.getStatus() != BoardStatus.BEFORE){
                throw new Exception("처리 전 상태에서만 변경 가능");
            }

            apartBoardService.updateBoard(boardId, boardDto);

            resultMap.put("message", "success");
            resultMap.put("id", boardId);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    //아파트 신고접수 수정 - 관리자
    @GetMapping("/apartManager/updateStatus/apartBoard/{apartBoardId}/{boardStatus}")
    public  ResponseEntity<Map<String, Object>> updateStatusApartBoard(
            @PathVariable("apartBoardId") int boardId, @PathVariable("boardStatus") String boardStatus) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        System.out.println("boardId= "+boardId);

        try {
            Integer loginId = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
            ApartManager loginUser = (ApartManager)memberService.findById(loginId);

             ApartBoard findBoard = apartBoardService.getBoardById(boardId);

             if(findBoard.getApart().getId() != loginUser.getApart().getId()){
                 throw new EOFException("권한이 없습니다. 담당 아파트가 아닙니다");
             }

            apartBoardService.updateBoardStatus(boardId, boardStatus);

            resultMap.put("message", "success");
            resultMap.put("id", boardId);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    //신고접수 삭제
    @GetMapping("/apart/deleteBoard/{apartBoardId}")
    public  ResponseEntity<Map<String, Object>> deleteApartBoard(
            @PathVariable("apartBoardId") int boardId) throws IllegalStateException, IOException  {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        try {

            ApartBoard board = apartBoardService.getBoardById(boardId);
            Member loginUser = memberService.findById(Integer.parseInt(id));

            if (loginUser.getRole() == Role.APART_MANAGER) {
                if (((ApartManager) loginUser).getApart().getId() != board.getApart().getId()) throw new Exception("권한이 없습니다");
            } else if (loginUser.getRole() == Role.APART_MEMBER) {
                if (((ApartMember) loginUser).getId() !=  board.getApartMember().getId()) throw new Exception("권한이 없습니다");
            } else {
                throw new Exception("권한이 없습니다");
            }

            apartBoardService.deleteBoard(boardId);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


}
