package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // 스프링 컨테이너 안에서 테스트를 수행함. @Autowired 를 사용할 수 있음.
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입")
//    @Rollback(false) // 테스트에 @Transactional 이 적용되어 있어 기본적으로 rollback 을 하는데, 해당 어노테이션으로 commit 을 시킬 수 있음.
    void joinTest() {
        // given
        Member member = new Member();
        member.setName("park");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));
        // 참고: JPA 에서 같은 @Transactional 안에 같은  엔티티 즉, ID(PK) 값이 같으면 같은 영속성 컨텍스트 안에서 동일한 객체를 보장함. (1개로 관리됨을 의미.)
    }

    @Test
    @DisplayName("중복 회원 예외")
    void duplicateUserException() {
        // given
        Member member1 = new Member();
        member1.setName("park");

        Member member2 = new Member();
        member2.setName("park");

        // when & then
        memberService.join(member1);
        assertThrows(IllegalStateException.class, () ->  memberService.join(member2));
    }
}