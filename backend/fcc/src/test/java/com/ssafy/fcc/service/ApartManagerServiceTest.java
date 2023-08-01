package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.member.ApartMember;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
public class ApartManagerServiceTest {

    @Autowired ApartManagerService apartManagerService;

    @Test
    @Transactional
    public void 입주민목록가져오기() throws Exception {
        //given
        int member_id = 2;

        //when

        //then
        List<ApartMember> members = apartManagerService.getMembersOfManager(member_id);
        for(ApartMember member : members){
            System.out.println("member = " + member.getApart().getFirstFloodMessage());
        }
    }
}