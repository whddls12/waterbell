package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.member.Role;
import com.ssafy.fcc.dto.BoardAlarmDto;
import com.ssafy.fcc.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class alarmController {

    private final AlarmService alarmService;

    @GetMapping("/{role}/{page}")
    public ResponseEntity<Map<String, Object>> getNotification(@PathVariable String role, @PathVariable("page") int page) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        if(!(role.equals(Role.APART_MANAGER.name()) || role.equals(Role.APART_MEMBER.name()) || role.equals(Role.PUBLIC_MANAGER.name())) ){
            status = HttpStatus.FORBIDDEN;
            resultMap.put("message", "fail");
            return new ResponseEntity<Map<String, Object>>(resultMap, status);
        }
        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        try{
            resultMap = alarmService.getAlarmList(member_id, page);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e){
            resultMap.put("message", "fail");
            resultMap.put("exception", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/{role}/detail/{id}")
    public ResponseEntity<BoardAlarmDto> getAlarmDetail(@PathVariable String role, @PathVariable Long id) {
        if(!(role.equals(Role.APART_MANAGER.name()) || role.equals(Role.APART_MEMBER.name()) || role.equals(Role.PUBLIC_MANAGER.name())) )
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!alarmService.checkUser(member_id, id)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        BoardAlarmDto alarm = alarmService.getAlarm(id);
        return new ResponseEntity<>(alarm, HttpStatus.OK);
    }


}
