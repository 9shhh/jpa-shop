package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

// @PersistenceContext -> JPA 표준 어노테이션, 스프링이 EntityManager 를 만들어서 주입해줌.
// spring boot data jpa 라이브러리를 사용하면, EntityManager 를 @Autowired 와 생성자 주입 스타일로 적용할 수 있다.
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); // 트랜잭션이 커밋 되는 시점에 insert 쿼리가 나가게 됨.
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();

        // JPQL 을 사용한 모든 사용자 조회.
        // JPQL 은 SQL 문과 유사함. -> 결국 SQL로 변환됨.
        // 약간의 문법 차이는 있음.
        // SQL 은 테이블을 대상으로, JPQL 은 엔티티 객체를 대상으로 질의함.
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
