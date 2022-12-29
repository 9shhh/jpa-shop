package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    // 객체는 컬렉션이 있어 다대다 관계를 표현할 수 있으나, 관계형 DB는 일대다 <- "연관관계 테이블" -> 다대일 로 풀어내야함.(연관관계 테이블 정의가 필요함.)
    // 실무에서는 다대다 관계를 사용하면 안됨. 필드를 더 추가하거나 하는 상황이 존재하는데 그게 불가능한 구조임.
    private List<Item> items = new ArrayList<>();


    // 셀프 연관관계 매핑 (
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
    // )

    //==연관관계 메소드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
