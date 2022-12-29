package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue // 시퀀스 값을 사용.
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입
    private Address address;

    @OneToMany(mappedBy = "member") // order 의 member 필드의 의해서 맵핑됨을 의미.(매핑된 거울을 의미.) -> 읽기 전용.
    private List<Order> orders = new ArrayList<>();
}
