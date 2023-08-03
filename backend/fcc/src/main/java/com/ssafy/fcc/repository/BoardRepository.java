package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.Image;
import com.ssafy.fcc.domain.board.UndergroundRoadBoard;
import com.ssafy.fcc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public Integer saveUndergroundRoadBoard(UndergroundRoadBoard undergroundRoadBoard){
        em.persist(undergroundRoadBoard);
        return undergroundRoadBoard.getId();
    }

    public List<UndergroundRoadBoard> dashUndergoundBoardList(int facilityId) {

        String jpqlQuery = "SELECT COUNT(b) FROM UndergroundRoadBoard b WHERE b.undergroundRoad.id = :undergroundRoadId";
        TypedQuery<Long> countQuery = em.createQuery(jpqlQuery, Long.class)
                .setParameter("undergroundRoadId", facilityId);
        Long count = countQuery.getSingleResult();

        String latestQuery = "SELECT b FROM UndergroundRoadBoard b WHERE b.undergroundRoad.id = :undergroundRoadId ORDER BY b.createDate DESC";
        List<UndergroundRoadBoard> resultList =null;
        if(count>5){
        resultList = em.createQuery(latestQuery, UndergroundRoadBoard.class)
                .setParameter("undergroundRoadId", facilityId)
                .setMaxResults(5)
                .getResultList();
        }else {
            resultList = em.createQuery(latestQuery, UndergroundRoadBoard.class)
                    .setParameter("undergroundRoadId", facilityId)
                    .getResultList();
        }

        return resultList;


    }
}
