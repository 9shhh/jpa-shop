package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //SINGLE_TABLE: 한 테이블에 컬럼을 다 넣는 형식. JOINED: 정규화된 형식. TABLE_PER_CLASS: 서브 타입을 명확히 구분하는 형식.
@DiscriminatorColumn(name = "dtype") //InheritanceType.SINGLE_TABLE 을 사용하므로 저장될 때 데이터의 구분자 정의 필요. -> dtype 으로 지정.
@Getter
@Setter // 실습을 위한 setter 를 정의한 것 뿐 실제 서비스 개발에는 setter 를 제공해 외부에서 값을 계산해서 넣는것이 아니라 해당 엔티티 안에서 그러한 것을 처리하는 비즈니스 로직을 구현하고, 해당 메소드를 제공하는 것이 올바른 형태이다.
public abstract class Item { //구현체를 만들도록 추상클래스로 생성함.

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    //도메인 주도 설계에서 엔티티 내 비즈니스 로직으로 처리 가능한 것들은 처리해 주는 것이 좋은 방법임.
    //데이터를 가지고 있는 주체에서 비즈니스 로직이 구현되고 제공되는게 응집도가 높은 코드라고 할 수 있음.
    //현 엔티티 기준에서는 재고수량을 엔티티 내 비즈니스 로직으로 구현하고 제공하는 형태로 한다.

    /**
     * 재고 증가
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 차감
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStock;
    }
}
