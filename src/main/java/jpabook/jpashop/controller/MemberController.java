package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {
        // @Valid 를 통해 오류가 발생하면, BindingResult 에 오류가 담긴채로 해당 코드를 쭉 실행함.
        // spring과 thymeleaf는 통합이 잘 되어 있음.
        // 오류가 발생하면 그 오류를 spring 이 화면까지 잘 끌고 가줌. -> 어떤 에러가 발생했는지 지정한 화면에서 보여줄 수 있음.

        // 추가로, 현재 엔티티와 유사한 도메인 클래스를 생성해 파라미터로 받게함.
        // 내용을 보면 Member 엔티티와 유사해서 그 타입으로 지정해서 사용할 수 있다고 생각하지만 실무에서는 엔티티를 가지고 폼 데이터를 받기에는 컨셉이 안맞는 경우가 허다함.
        // 그래서 엔티티와 폼 전용 도메인 클래스를 따로 정의해서 관리해야함.

        if (bindingResult.hasErrors()) {
            // MemberForm, BindingResult 데이터를 가지고 해당 뷰를 렌더링 함.(spring + thymeleaf 가 지원함.)
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
