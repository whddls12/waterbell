package com.ssafy.fcc.controller;

import com.ssafy.fcc.config.security.JwtTokenProvider;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.Member;
import com.ssafy.fcc.domain.member.PublicManager;
import com.ssafy.fcc.domain.member.Role;
import com.ssafy.fcc.dto.FacilityManagementDto;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
public class ManagementController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final FacilityService facilityService;

    @GetMapping("/manager/view/controlInfo/{facilityId}")
    public  ResponseEntity<Map<String, Object>>  viewFacilityManagementInfo(@PathVariable Integer facilityId) {


        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        int id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Member loginUser =  memberService.findById(id);
        Facility facility = facilityService.findById(facilityId);


        try{
            if (loginUser.getRole() == Role.APART_MANAGER) {
                if (((ApartManager)loginUser).getApart().getId().intValue() != facilityId)
                    throw new Exception("권한이 없습니다.");

            }else if (loginUser.getRole() == Role.PUBLIC_MANAGER){
                if (((PublicManager)loginUser).getSido() != facility.getGugun().getSido() || facility.isApart() )
                    throw new Exception("권한이 없습니다.");
            }else{
                throw new Exception("권한이 없습니다.");
            }

            FacilityManagementDto dto = new FacilityManagementDto(
                    facility.getId(), facility.getFirstFloodMessage(),
                    facility.getActivation_message(), facility.getDeactivation_message(),
                    facility.getFirstAlarmValue(), facility.getSecondAlarmValue()
            );

            resultMap.put("facility",dto);
            resultMap.put("message", "success");

            status = HttpStatus.ACCEPTED;

        } catch (Exception e) {
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


    @PostMapping("/manager/modify/controlInfo/{facilityId}")
    public  ResponseEntity<Map<String, Object>>  modifyFacilityManagementInfo(@PathVariable Integer facilityId,@RequestBody FacilityManagementDto dto) {


        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        int id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        Member loginUser =  memberService.findById(id);
        Facility facility = facilityService.findById(facilityId);


        try{
            if (loginUser.getRole() == Role.APART_MANAGER) {
                if (((ApartManager)loginUser).getApart().getId().intValue() != facilityId)
                    throw new Exception("권한이 없습니다.");

            }else if (loginUser.getRole() == Role.PUBLIC_MANAGER){
                if (((PublicManager)loginUser).getSido() != facility.getGugun().getSido() || facility.isApart() )
                    throw new Exception("권한이 없습니다.");
            }else{
                throw new Exception("권한이 없습니다.");
            }

           Integer modifyFacilityId= facilityService.modifyManagementInfo(facilityId,dto);

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
