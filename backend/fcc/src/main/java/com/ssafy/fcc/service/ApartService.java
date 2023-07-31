package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.repository.ApartManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApartService {

    private final ApartManagerRepository apartManagerRepository;

    // 1차, 2차 자동알림
    @Transactional
    public void sendAutoNotification(int member_id, WaterStatus status){
        ApartManager manager = apartManagerRepository.findById(member_id); // 해당 시설 관리자
        // 1차 경고 상황
        if(status.name().equals(WaterStatus.FIRST.name())){

        }
        // 2차 경고 상황
        else {

        }

        // 웹 알림

        // 문자 알림
    }
}
