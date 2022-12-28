package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional // 해당 어노테이션이 테스트에 있으면 rollback 을 처리함.
    @Rollback(false) // 자동 롤백 싫으면 해당 어노테이션으로 false 주면됨.
    void testMember() {
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        System.out.println("findMember == member: " + (findMember == member));
        // 같은 영속성 컨텍스트 안에서 식별자가 같으면 같은 엔티티로 인식함.
        // 추가로, 영속성 컨텍스트에 이미 해당 엔티티가 존재하고 있어 select query 를 실행하지 않고 1차 캐시 안에서 엔티티를 반환해버림.
    }
}