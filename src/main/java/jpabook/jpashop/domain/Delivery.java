package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "deliver_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // EnumType.ORDINAL 속성은 숫자로 들어감. 중간에 xxx 의 다른 값이 추가되면 데이터 정합성이 안맞게 되어 장애남.(사용하면 안됨.) -> EnumType.STRING 을 쓰자.
    private DeliveryStatus deliveryStatus; // READY, COMP
}
