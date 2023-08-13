package com.ssafy.fcc.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.fcc.domain.alarm.BoardAlarmLog;
import com.ssafy.fcc.domain.alarm.ReceiveAlarmMember;
import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.Image;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.dto.AlarmLogDto;
import com.ssafy.fcc.handler.MyWebSocketHandler;
import com.ssafy.fcc.repository.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

}
