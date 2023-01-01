package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자가 없는 생성자를 protected 레벨로 생성해줌.
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 당시 가격

    private int count; //주문 당시 수량

    // protected OrderItem() {} => jpa 는 protected 까지 기본 생성자를 만들 수 있음. 그리고, jpa 에서 protected 생성자를 선언했다는 것은 new 로 객체 생성을 하지 말라는 의미임.
    // createOrderItem 이라는 생성 메소드로 데이터 생성을 처리하게 했지만, setter 를 통한 데이터 추가 및 생성도 가능하기 때문에 protected 생성자를 통해 객체 생성을 제한함.

    //==생성 메소드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 주문 상품 만큼 재고 차감
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        // 주문 수량 만큼 재고를 원복 시킴.
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
