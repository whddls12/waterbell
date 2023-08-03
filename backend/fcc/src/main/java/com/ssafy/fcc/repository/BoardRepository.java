package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;


    public Integer saveApartBoard(ApartBoard apartBoard){
        em.persist(apartBoard);
        return apartBoard.getId();
    }

    public Integer saveIamge(Image image) {
        em.persist(image);
        return image.getId();
    }
}
