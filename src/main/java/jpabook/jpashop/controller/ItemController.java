package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();

        // 현재 코드는 setter 를 통한 값 셋팅이지만, Order 엔티티의 createOrder 와 같은 생성 메소드를 만들어서 처리하는게 바람직한 방법임.
        // 실무에서는 static 생성자(생성 메소드)로 처리하자.
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";

    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); // 예제를 심플하게 하기 위한 코드임. 실제로 이렇게 타입 캐스트 하는 방법은 좋지 않음.

        BookForm form = new BookForm(); // setter 를 통한 값 셋팅은 실제론 권장하지 않음. 코드 분석에 혼란을 줌. -> 의미 있는 메소드를 생성해 값을 셋팅하자.
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

//    @PostMapping("/items/{itemId}/edit")
//    public String updateItem(@ModelAttribute("form") BookForm form) {
//
//        Book book = new Book(); // setter 를 통한 값 셋팅은 실제론 권장하지 않음. 코드 분석에 혼란을 줌. -> 의미 있는 메소드를 생성해 값을 셋팅하자.
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//
//        추가로, 컨트롤러 계층에서 어설프게 엔티티를 생성하지 말자. 아래의 updateItem 메소드 처럼 식별자와 파라미터 또는 dto 를 생성해서 넘기자.
//
//        itemService.saveItem(book);
//        return "redirect:/items";
//    }

    /**
     * 상품 수정,권장 코드
     */
    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
