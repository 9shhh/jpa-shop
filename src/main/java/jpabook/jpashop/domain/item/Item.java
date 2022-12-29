package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //SINGLE_TABLE: 한 테이블에 컬럼을 다 넣는 형식. JOINED: 정규화된 형식. TABLE_PER_CLASS: 서브 타입을 명확히 구분하는 형식.
@DiscriminatorColumn(name = "dtype") //InheritanceType.SINGLE_TABLE 을 사용하므로 저장될 때 데이터의 구분자 정의 필요. -> dtype 으로 지정.
@Getter
@Setter
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
}
