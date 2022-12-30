package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
/**
 * == Transactional 어노테이션 ==
 * JPA 의 모든 데이터 변경이나, 어떤 로직들은 가급적 트랜잭션 안에서 실행되어야 함. -> 그래야 LAZY 로딩 등 이 적용됨.
 * 추가로, 자바도 트랜잭션 어노테이션을 제공하지만 스프링에서 제공하는 트랜잭션 어노테이션을 사용하자. -> 제공되는 옵션이 많음.
 * @Transactional(readOnly = true) 해당 어노테이션 및 옵션을 주면 JPA 조회하는 로직에 성능을 좀 더 최적화 함.
 * 따라서, "읽기(조회)"에는 가급적 해당 속성을 지정해주자.
 */
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional // 쓰기 작업인 가입은 "readOnly=false" 인 기본 @Transactional 을 지정함.
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        return member.getId(); // em.persist(member) 에 의해 영속성 컨텍스트에 등록된 객체의 키값인 id를 리턴함. -> 디비가 아닌 영속성 컨테이너에서 해당 id(PK) 값으로 조회 가능.
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        /**
         * 해당 validation 로직은 연습을 위한 코드로 부족한 부분이 많음.
         * 가장 문제가 되는게 만약 다수의 유저가 동시에 "userA" 라는 이름으로 가입하게 되었을 때 통과 될 수 있다.
         * 따라서, 실무에서는 멀티쓰레드 같은 상황도 고려하여 데이터베이스 member 테이블의 name 컬럼에 unique 제약조건을 설정하자.
         */
    }
}
