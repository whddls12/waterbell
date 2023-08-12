package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.board.ApartBoard;
import com.ssafy.fcc.domain.board.Image;
import com.ssafy.fcc.domain.board.UndergroundRoadBoard;
import com.ssafy.fcc.domain.member.Member;
import io.swagger.models.auth.In;
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

    public Image findImageById(Integer id) {
        return em.find(Image.class, id);
    }


    public void deleteImage(Image image) {
        em.remove(image);
    }

    public void deleteUndergroundRoadBoard(UndergroundRoadBoard undergroundRoadBoard) {
        em.remove(undergroundRoadBoard);
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

    public List<UndergroundRoadBoard> getUndergoundBoadList(int facilityId, int start, int size) {

        String latestQuery = "SELECT b FROM UndergroundRoadBoard b WHERE b.undergroundRoad.id = :undergroundRoadId ORDER BY b.createDate DESC, b.id DESC ";
        List<UndergroundRoadBoard> resultList = em.createQuery(latestQuery, UndergroundRoadBoard.class)
                .setParameter("undergroundRoadId", facilityId)
                .setFirstResult(start) // 시작 위치
                .setMaxResults(size) // 가져올 개수
                .getResultList();
        return resultList;
    }

    public Long getUndergroundBoardCnt(int facilityId) {
        String jpqlQuery = "SELECT COUNT(b) FROM UndergroundRoadBoard b WHERE b.undergroundRoad.id = :undergroundRoadId";
        TypedQuery<Long> countQuery = em.createQuery(jpqlQuery, Long.class)
                .setParameter("undergroundRoadId", facilityId);
        return countQuery.getSingleResult();
    }

    public UndergroundRoadBoard getUndergoundBoardById(int boardId) {
        return em.find(UndergroundRoadBoard.class,boardId);
    }

    public List<Image> getImageByUndergoundRoadBoardId(int boardId) {
        String latestQuery = "SELECT i FROM Image i WHERE  i.undergroundRoadBoard.id = :boardId ";
        List<Image> resultList = em.createQuery(latestQuery, Image.class)
                .setParameter("boardId", boardId)
                .getResultList();
        return resultList;
    }
}
