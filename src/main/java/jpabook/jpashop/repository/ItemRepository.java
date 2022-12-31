package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { //id 값이 없다는 것은 완전히 새로운 객체를 생성함을 의미함.
            em.persist(item);
        } else {
            em.merge(item);
            //merge(entity) 는 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용함.
            //준영속 상태는 영속성 컨텍스트로부터 "분리된 상태"로 영속성 컨텍스트가 관리하지 않는 상태를 의미함.
            //merge(entity)는 준영속, 비영속을 신경쓰지 않음.
            //"식별자 값"으로 엔티티를 조회 가능하면 불러서 병합, 조회가 불가하면 새로 생성해서 병합.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
