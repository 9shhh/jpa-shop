package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("상품 주문")
    void order() {
        //given
        Member member = createMember("park");

        Item item = createBook("JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);
        assertThat(OrderStatus.ORDER).isEqualTo(order.getStatus()); // 주문 상태 검증
        assertThat(1).isEqualTo(order.getOrderItems().size()); // 주문 상품 종류 수 검증
        assertThat(10000 * orderCount).isEqualTo(order.getTotalPrice()); // 주문 가격 = 가격 * 수량 검증
        assertThat(8).isEqualTo(item.getStockQuantity()); // 주문 수량만큼 재고 개수 차감 검증

    }

    @Test
    @DisplayName("상품 주문 재고 수량 초과")
    void stockQuantityExceeded() {
        //given
        Member member = createMember("park");
        Item item = createBook("JPA", 10000, 10);

        int orderCount = 11;

        //when & then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getId(), orderCount));
    }

    @Test
    @DisplayName("주문 취소")
    void cancel() {
        //given
        Member member = createMember("park");
        Item item = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findOne(orderId);

        assertThat(OrderStatus.CANCEL).isEqualTo(order.getStatus()); // 주문 취소 상태 검증
        assertThat(10).isEqualTo(item.getStockQuantity()); // 주문 취소시 상품은 그만큼 재고 증가 검증
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "강남구", "111-222"));
        em.persist(member);
        return member;
    }
}