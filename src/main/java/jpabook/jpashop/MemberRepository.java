package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository { // Repository -> DAO 같은 역할.

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
        // command 와 query 를 분리 -> 사이드 이팩트를 줄이고, 데이터가 필요하면 조회를 위한 아이디 정보만 리턴하여 활용함.
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
