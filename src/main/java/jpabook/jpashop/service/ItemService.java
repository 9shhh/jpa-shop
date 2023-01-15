package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 영속성 컨텍스트가 자동 변경
     */
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity) {
        Item item = itemRepository.findOne(id); // id 기반으로 통해 실제 DB에 있는 '영속 상태'인 데이터를 찾아옴.
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        /*
          값을 다 셋팅하고 이 라인에 오면(더 이상 수행할 코드가 없음.) 스프링의 @Transactional 에 의해 커밋이 됨.
          JPA 는 커밋이 되면 flush 를 수행함.
          flush 가 동작하면 영속성 컨텍스트의 엔티티 중에 변경된 내용을 찾음.
          변경된 값을 찾으면 DB에 update 쿼리를 날림.
          이게 변경 감지에 의한 데이터 수정임.
         */
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
