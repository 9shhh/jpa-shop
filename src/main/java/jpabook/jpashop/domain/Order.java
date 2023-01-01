package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 해당 필드가 연관관계의 주인임.

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //cascade = CascadeType.ALL -> Order persist 시 OrderItem 도 함께 persist 되는 속성.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) //cascade = CascadeType.ALL -> Order persist 시 Delivery 도 함께 persist 됨.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간 (LocalDateTime 사용하면 Hibernate 가 자동으로 해당 타입을 지원함. -> 별도 맵핑 불필요)

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 메소드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메소드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem ...orderItems) {
        // 외부에 Order 를 new 해서 set...(...), 하는 방식이 아니라, 주문 생성에 대한 복잡한 로직을 생성 메소드를 통해 완결을 시킴.
        // 따라서, 주문 생성에 대한 부분을 수정하려면 해당 주문 생성 메소드만 수정 하면 된다.

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        // 주문 취소 유효 판단
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송이 완료된 상품은 취고사 불가능합니다.");
        }

        // 취소가 유효 하여 취소 상태로 변경 및 취소
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            // 주문 상품 개별의 (가격 * 수량) 한 가격을 모두 더함.
            // ex) 한 주문에 A책(10000원) 3권 + B첵(15000) * 3권
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
