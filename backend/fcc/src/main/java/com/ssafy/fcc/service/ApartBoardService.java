package com.ssafy.fcc.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.BoardStatus;
import com.ssafy.fcc.domain.board.Image;
import com.ssafy.fcc.domain.board.UndergroundRoadBoard;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.dto.AlarmLogDto;
import com.ssafy.fcc.dto.DashApartBoardResponseDto;
import com.ssafy.fcc.dto.DashUndergroundRoadBoardResponseDto;
import com.ssafy.fcc.dto.UpdateApartBoardRequestDto;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.repository.*;
import com.ssafy.fcc.util.PageNavigation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApartBoardService {

    private final ApartMemberRepository apartMemberRepository;
    private final ApartManagerRepository apartManagerRepository;
    private final MemberRepository memberRepository;
    private final BoardAlarmLogRepository boardAlarmLogRepository;
    private final ReceiveAlarmMemberRepository receiveAlarmMemberRepository;
    private final MyWebSocketHandler myWebSocketHandler;
    private final BoardRepository boardRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    // 글 작성시 관리자에게 웹 알림
    @Transactional
    public void sendWebNotification(int member_id, int board_id) throws IOException {
        ApartMember member = apartMemberRepository.findById(member_id);
        ApartManager manager = apartManagerRepository.findByFacility(member.getApart().getId());
        String notificationMessage = "신고접수 게시판에 " + member.getName()+ "님의 접수가 새로 등록되었습니다.";

        // 신고접수 알림로그 저장
        BoardAlarmLog boardAlarmLog = new BoardAlarmLog();
        boardAlarmLog.setMember(memberRepository.getSystemMember());
        boardAlarmLog.setFacility(member.getApart());
        boardAlarmLog.setRegDate(LocalDateTime.now());
        boardAlarmLog.setContent(notificationMessage);
        boardAlarmLog.setIsApart(true);
        boardAlarmLog.setIsFlood(false);
        boardAlarmLog.setApartBoardId(board_id);
        boardAlarmLogRepository.save(boardAlarmLog);
        AlarmLogDto alarmLogDto = new AlarmLogDto(boardAlarmLog);

        // 웹 알림 보내고 저장
        ReceiveAlarmMember receiveAlarmMember = new ReceiveAlarmMember();
        receiveAlarmMember.setAlarm(boardAlarmLog);
        receiveAlarmMember.setMember(manager);
        receiveAlarmMember.setRead(false);
        receiveAlarmMemberRepository.save(receiveAlarmMember);
        myWebSocketHandler.sendNotificationToSpecificUser(manager.getLoginId(), alarmLogDto);
    }

    @Transactional
    public Integer writeApartBoard(ApartBoard apartBoard, List<MultipartFile> uploadedfiles) {

        Integer apartBoardId = boardRepository.saveApartBoard(apartBoard);

        if(uploadedfiles != null &&uploadedfiles.size()>0) {

            uploadedfiles.forEach(file -> {
                String temp= null;
                try {
                    String fileName = createFileName(file.getOriginalFilename());
                    temp = uploadImg(file,fileName);
                    System.out.println("=================img==============================");
                    System.out.println("파일 URL="+temp);
                    Image image = new Image();
                    image.setApartBoard(apartBoard);
                    image.setImageName(file.getOriginalFilename());
                    image.setImagePath(fileName);
                    System.out.println(image);
                    Integer imageId = boardRepository.saveIamge(image);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }
                System.out.println(temp);
            });


        }

        return apartBoardId;
    }

    public String uploadImg(MultipartFile multipartFile, String fileName) throws IOException {
        // String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objMeta);
        return amazonS3.getUrl(bucket, fileName).toString();
    }
    private String createFileName(String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    public List<DashApartBoardResponseDto> getBoadListLatest(int facilityId) {
         List<ApartBoard> apartBoards = boardRepository.dashApartList(facilityId);

        List<DashApartBoardResponseDto> list = new ArrayList<>();

        if (apartBoards != null && apartBoards.size() > 0) {

            for (ApartBoard b : apartBoards) {
                DashApartBoardResponseDto boardResponseDto = new DashApartBoardResponseDto();
                boardResponseDto.setId(b.getId());
                boardResponseDto.setStatus(b.getStatus());
                boardResponseDto.setTitle(b.getTitle());
                boardResponseDto.setCreateDate(b.getCreateDate());

                list.add(boardResponseDto);
            }
            System.out.println(list);
        } else {
            throw new RuntimeException("데이터가 없습니다.");
        }
        return list;
    }

    @Transactional
    public Map<String, Object> getBoadListByPage(int facilityId, int page) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Long totalCount = boardRepository.getApartBoardCnt(facilityId);
        PageNavigation pageNavigation = new PageNavigation(page, totalCount);
        List<ApartBoard> boardList = boardRepository.getApartBoardList(facilityId, pageNavigation.getStart(), pageNavigation.getSizePerPage());

        if (boardList == null || boardList.size() == 0)
            throw new RuntimeException("데이터가 없습니다.");
        resultMap.put("pageNavigation", pageNavigation);
        resultMap.put("list", boardList);
        System.out.println(boardList);
        return resultMap;
    }

    @Transactional
    public ApartBoard getBoardById(int boardId) throws Exception {
        ApartBoard board = boardRepository.getapartBoardById(boardId);
        if (board == null) throw new RuntimeException("데이터가 없습니다.");
        return board;
    }


    public List<Image> getImageByBoardId(int boardId) {

        List<Image> imageList = boardRepository.getImageByApartBoardId(boardId);
        System.out.println(imageList);
        for (Image image : imageList) {
            image.setUrl(amazonS3.getUrl(bucket, image.getImagePath()).toString());
        }
        return imageList;
    }

    @Transactional
    public void updateBoard(int boardId, UpdateApartBoardRequestDto boardDto) throws Exception {

         ApartBoard apartBoard = boardRepository.getapartBoardById(boardId);
        apartBoard.setTitle(boardDto.getTitle());
        apartBoard.setContent(boardDto.getContent());


        if(boardDto.getRemovefiles() !=null && boardDto.getRemovefiles().size()>0){
            for(Integer imageId : boardDto.getRemovefiles()){
                //이미지 테이블에서 조회해와서 S3에서 이미지 지우고,  이미지 테이블에서 지운다.
                final Image image = boardRepository.findImageById(imageId);
                deleteImage(image.getImagePath());
                boardRepository.deleteImage(image);
            }}

        //추가하는 이미지 추가
        List<MultipartFile> uploadedfiles = boardDto.getAddUploadedfiles();
        if (uploadedfiles != null && uploadedfiles.size() > 0) {

            uploadedfiles.forEach(file -> {
                String temp = null;
                try {
                    String fileName = createFileName(file.getOriginalFilename());
                    temp = uploadImg(file, fileName);
                    System.out.println("=================img==============================");
                    System.out.println("파일 URL=" + temp);
                    Image image = new Image();
                    image.setApartBoard(apartBoard);
                    image.setImageName(file.getOriginalFilename());
                    image.setImagePath(fileName);
                    System.out.println(image);
                    Integer imageId = boardRepository.saveIamge(image);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }
                System.out.println(temp);
            });
        }
    }

    public void deleteImage( String fileName) throws IOException {

        try {
            amazonS3.deleteObject(bucket, fileName);
        }catch (SdkClientException e){
            throw  new IOException("error deleting file from s3",e);
        }
    }


    @Transactional
    public void updateBoardStatus(int boardId, String boardStatus) throws Exception {

        ApartBoard apartBoard = boardRepository.getapartBoardById(boardId);

        if(boardStatus.equals("BEFORE"))
            apartBoard.setStatus(BoardStatus.BEFORE);
        else  if(boardStatus.equals("PROCESSING"))
            apartBoard.setStatus(BoardStatus.PROCESSING);
        else  if(boardStatus.equals("COMPLETE"))
            apartBoard.setStatus(BoardStatus.COMPLETE);
        else throw new Exception("존재하지 않은 상태 요청입니다.");
    }

    @Transactional
    public void deleteBoard(int boardId) throws Exception{

        //이미지 다 지우고, 게시글 삭제
        List<Image> imageList = boardRepository.getImageByApartBoardId(boardId);
        System.out.println(imageList);
        for (Image image : imageList) {
            deleteImage(image.getImagePath());
            boardRepository.deleteImage(image);
        }
         ApartBoard apartBoard = boardRepository.getapartBoardById(boardId);

        boardRepository.deleteApartBoard(apartBoard);
    }


    @Transactional
    public void updateViewCount(int boardId) {
        ApartBoard apartBoard = boardRepository.getapartBoardById(boardId);
        apartBoard.setViewCount(apartBoard.getViewCount()+1);
    }

}
