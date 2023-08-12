package com.ssafy.fcc.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.board.BoardStatus;
import com.ssafy.fcc.domain.board.Image;
import com.ssafy.fcc.domain.board.UndergroundRoadBoard;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.member.PublicManager;
import com.ssafy.fcc.dto.AlarmLogDto;
import com.ssafy.fcc.dto.DashUndergroundRoadBoardResponseDto;
import com.ssafy.fcc.dto.UpdateUndergroundRoadBoardRequestDto;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.repository.*;
import com.ssafy.fcc.util.PageNavigation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UndergroundRoadBoardService {

    private final PublicManagerRepository publicManagerRepository;
    private final FacilityRepository facilityRepository;
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
    public void sendWebNotification(int facility_id, int board_id) throws IOException {
        Facility facility = facilityRepository.findById(facility_id);
        PublicManager manager = publicManagerRepository.findBySido(facility.getGugun().getSido().getId());
        String notificationMessage = "신고접수 게시판에 새로운 접수가 등록되었습니다.";

        // 신고접수 알림로그 저장
        BoardAlarmLog boardAlarmLog = new BoardAlarmLog();
        boardAlarmLog.setMember(memberRepository.getSystemMember());
        boardAlarmLog.setFacility(facility);
        boardAlarmLog.setRegDate(LocalDateTime.now());
        boardAlarmLog.setContent(notificationMessage);
        boardAlarmLog.setIsApart(false);
        boardAlarmLog.setIsFlood(false);
        boardAlarmLog.setUndergroundBoardId(board_id);
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
    public Integer undergroundRoadBoard(UndergroundRoadBoard undergroundRoadBoard, List<MultipartFile> uploadedfiles) throws Exception {

        Integer undergroundRoadBoardId = boardRepository.saveUndergroundRoadBoard(undergroundRoadBoard);

        if (uploadedfiles != null && uploadedfiles.size() > 0) {
//            final List<String> fileList = uploadFile(uploadedfiles, undergroundRoadBoard);
//            System.out.println(fileList);
            uploadedfiles.forEach(file -> {
                String temp = null;
                try {
                    String fileName = createFileName(file.getOriginalFilename());
                    temp = uploadImg(file, fileName);
                    System.out.println("=================img==============================");
                    System.out.println("파일 URL=" + temp);
                    Image image = new Image();
                    image.setUndergroundRoadBoard(undergroundRoadBoard);
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

        return undergroundRoadBoardId;
    }


    public String uploadImg(MultipartFile multipartFile, String fileName) throws IOException {
        // String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objMeta);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteImage( String fileName) throws IOException {

        try {
            amazonS3.deleteObject(bucket, fileName);
        }catch (SdkClientException e){
            throw  new IOException("error deleting file from s3",e);
        }
    }


    public List<String> uploadFile(List<MultipartFile> multipartFile, UndergroundRoadBoard undergroundRoadBoard) throws Exception {
        List<String> fileNameList = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가

        multipartFile.forEach(file -> {

            Image image = new Image();
            image.setUndergroundRoadBoard(undergroundRoadBoard);
            image.setImageName(file.getOriginalFilename());


            String fileName = createFileName(file.getOriginalFilename());
            image.setImagePath(fileName);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                System.out.println("image= " + image);
                try {
                    amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
                } catch (Exception e) {
                    System.out.println("===============================================================");
                    System.out.println(e.getMessage());
                }
                Integer imageId = boardRepository.saveIamge(image);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

            fileNameList.add(fileName);
        });

        return fileNameList;
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


    @Transactional
    public List<DashUndergroundRoadBoardResponseDto> getBoadListLatest(int facilityId) throws Exception {

        final List<UndergroundRoadBoard> undergroundRoadBoards = boardRepository.dashUndergoundBoardList(facilityId);

        List<DashUndergroundRoadBoardResponseDto> list = new ArrayList<>();

        if (undergroundRoadBoards != null && undergroundRoadBoards.size() > 0) {

            for (UndergroundRoadBoard b : undergroundRoadBoards) {
                DashUndergroundRoadBoardResponseDto dd = new DashUndergroundRoadBoardResponseDto();
                dd.setId(b.getId());
                dd.setStatus(b.getStatus());
                dd.setTitle(b.getTitle());
                dd.setCreateDate(b.getCreateDate());

                list.add(dd);
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

        Long totalCount = boardRepository.getUndergroundBoardCnt(facilityId);
        PageNavigation pageNavigation = new PageNavigation(page, totalCount);

        List<UndergroundRoadBoard> undergroundRoadBoards = boardRepository.getUndergoundBoadList(facilityId, pageNavigation.getStart(), pageNavigation.getSizePerPage());
        if (undergroundRoadBoards == null || undergroundRoadBoards.size() == 0)
            throw new RuntimeException("데이터가 없습니다.");
        resultMap.put("pageNavigation", pageNavigation);
        resultMap.put("list", undergroundRoadBoards);
        System.out.println(undergroundRoadBoards);

        return resultMap;
    }


    @Transactional
    public UndergroundRoadBoard getBoardById(int boardId) throws Exception {
        UndergroundRoadBoard board = boardRepository.getUndergoundBoardById(boardId);
        if (board == null) throw new RuntimeException("데이터가 없습니다.");
        return boardRepository.getUndergoundBoardById(boardId);
    }

    @Transactional
    public List<Image> getImageByBoardId(int boardId) {

         List<Image> imageList = boardRepository.getImageByUndergoundRoadBoardId(boardId);
        System.out.println(imageList);
        for (Image image : imageList) {
            image.setUrl(amazonS3.getUrl(bucket, image.getImagePath()).toString());
        }
        return imageList;
    }


    @Transactional
    public void updateBoard(int boardId, UpdateUndergroundRoadBoardRequestDto boardDto) throws Exception{

        final UndergroundRoadBoard undergroundRoadBoard = boardRepository.getUndergoundBoardById(boardId);
        undergroundRoadBoard.setTitle(boardDto.getTitle());
        undergroundRoadBoard.setContent(boardDto.getContent());
        undergroundRoadBoard.setName(boardDto.getName());
        undergroundRoadBoard.setBoardPassword(boardDto.getBoardPassword());


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
                    image.setUndergroundRoadBoard(undergroundRoadBoard);
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

    @Transactional
    public void updateBoardStatus(int boardId, String boardStatus) throws Exception {

        final UndergroundRoadBoard undergroundRoadBoard = boardRepository.getUndergoundBoardById(boardId);

        if(boardStatus.equals("BEFORE"))
         undergroundRoadBoard.setStatus(BoardStatus.BEFORE);
        else  if(boardStatus.equals("PROCESSING"))
            undergroundRoadBoard.setStatus(BoardStatus.PROCESSING);
        else  if(boardStatus.equals("COMPLETE"))
            undergroundRoadBoard.setStatus(BoardStatus.COMPLETE);
        else throw new Exception("존재하지 않은 상태 요청입니다.");
    }

    @Transactional
    public void deleteBoard(int boardId) throws Exception{
        
        //이미지 다 지우고, 게시글 삭제
         List<Image> imageList = boardRepository.getImageByUndergoundRoadBoardId(boardId);
        System.out.println(imageList);
        for (Image image : imageList) {
            deleteImage(image.getImagePath());
            boardRepository.deleteImage(image);
        }
        final UndergroundRoadBoard undergroundRoadBoard = boardRepository.getUndergoundBoardById(boardId);

        boardRepository.deleteUndergroundRoadBoard(undergroundRoadBoard);
    }
}
