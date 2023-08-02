package com.ssafy.fcc.service;

import com.ssafy.fcc.domain.location.Gugun;
import com.ssafy.fcc.repository.GugunRepository;
import com.ssafy.fcc.repository.PublicManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GugunService {

    private final GugunRepository gugunRepository;
    private final PublicManagerRepository publicManagerRepository;
    public List<Gugun> findGugunAll(){
        List<Gugun> gugunList = gugunRepository.findAll();

        for(Gugun g : gugunList){
            System.out.println(g.toString());
        }
        if(gugunList.size()==0) return null;
        return gugunList;


    }
    public List<Gugun> findGugunByPmanager(Integer memberId){
        Integer sidoId = publicManagerRepository.findSidoByMemberId(memberId).getId();
        List<Gugun> gugunList = gugunRepository.findGugunBySido(sidoId);
        if(gugunList.size()==0 ) return null;
        return gugunList;
    }
}