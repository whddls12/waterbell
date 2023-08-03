package com.ssafy.fcc.controller;

import com.ssafy.fcc.domain.member.Role;
import com.ssafy.fcc.dto.BoardAlarmDto;
import com.ssafy.fcc.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class alarmController {

    private final AlarmService alarmService;

    @GetMapping("/{role}")
    public ResponseEntity<List<BoardAlarmDto>> getNotification(@PathVariable String role) {
        if(!(role.equals(Role.APART_MANAGER.name()) || role.equals(Role.APART_MEMBER.name()) || role.equals(Role.PUBLIC_MANAGER.name())) )
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        int member_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        List<BoardAlarmDto> alarmList = alarmService.getAlarmList(member_id);
        return new ResponseEntity<>(alarmList, HttpStatus.OK);
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
