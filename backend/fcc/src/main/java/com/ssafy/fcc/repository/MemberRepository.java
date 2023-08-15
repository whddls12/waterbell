package com.ssafy.fcc.repository;

import com.ssafy.fcc.domain.facility.Apart;
import com.ssafy.fcc.domain.member.ApartManager;
import com.ssafy.fcc.domain.member.ApartMember;
import com.ssafy.fcc.domain.member.Member;
import com.ssafy.fcc.domain.member.Role;
import com.ssafy.fcc.dto.MemberSearch;
import com.ssafy.fcc.util.PageNavigation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Member findById(int id) {
        Member member = em.find(Member.class, id);
        return member;
    }

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByLoginId(String loginId) {
        try {
            return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                    .setParameter("loginId", loginId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findLogin(String loginId, String password) {
        try {
            return em.createQuery("select m from Member m where m.loginId = :loginId and m.password = :password ", Member.class)
                    .setParameter("loginId", loginId)
                    .setParameter("password", password)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findByPhone(String phone) {
        try {
            return em.createQuery("select m from Member m where m.phone = :phone and m.role = :role ", Member.class)
                    .setParameter("phone", phone)
                    .setParameter("role", Role.APART_MEMBER)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member getSystemMember(){
        try {
            return em.createQuery("select m from Member m where m.role = :role ", Member.class)
                    .setParameter("role", Role.SYSTEM)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ApartMember findByPhoneAndName(String name, String phone) {

        try {
            return em.createQuery("select m from ApartMember m where m.phone = :phone and m.name = :name ", ApartMember.class)
                    .setParameter("phone", phone)
                    .setParameter("name", name)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public void deleteApartMember(ApartMember member){
       em.remove(member);
    }

    public Map<String, Object>  findAllByCriteria(MemberSearch memberSearch) {

        Map<String, Object> resultMap = new HashMap<>();


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ApartMember> cq = cb.createQuery(ApartMember.class);
        Root<ApartMember> a = cq.from(ApartMember.class);
//        Join<ApartMember, Apart> m = a.join("apart", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //호수 검색
        if (memberSearch.getAddressNumber() != null) {
            Predicate addressNumber = cb.equal(a.get("addressNumber"), memberSearch.getAddressNumber());
            criteria.add(addressNumber);
        }

        //회원 이름 검색
        if (StringUtils.hasText(memberSearch.getName())) {
            Predicate name =
                    cb.like(a.<String>get("name"), "%" +
                            memberSearch.getName() + "%");
            criteria.add(name);
        }

        // Subquery를 활용하여 아파트 코드 검색
        if (StringUtils.hasText(memberSearch.getApartCode())) {
            Subquery<Apart> subquery = cq.subquery(Apart.class);
            Root<Apart> m = subquery.from(Apart.class);
            subquery.select(m).where(cb.equal(m.get("apartCode"), memberSearch.getApartCode()));
            Predicate apartPredicate = cb.equal(a.get("apart"), subquery);
            criteria.add(apartPredicate);
        }

        //상태가 활성
        Predicate statePredicate = cb.isTrue(a.get("state"));
        criteria.add(statePredicate);

        //권한이 아파트
        Predicate apartRole = cb.equal(a.get("role"), Role.APART_MEMBER);
        criteria.add(apartRole);


        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        final TypedQuery<ApartMember> query = em.createQuery(cq);

        Long total = query.getResultStream().count();
        System.out.println(total);

        PageNavigation pageNavigation = new PageNavigation(memberSearch.getPage(), total);

        final List<ApartMember> resultList = query.setFirstResult(pageNavigation.getStart()) // 시작 위치
                                            .setMaxResults(pageNavigation.getSizePerPage()) // 가져올 개수
                                            .getResultList();

        resultMap.put("pageNavigation",pageNavigation);
        resultMap.put("list", resultList);

//         final List<ApartMember> resultList1 = em.createQuery(cq).getResultList();

        return resultMap;

    }
}
