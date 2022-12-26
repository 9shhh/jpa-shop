package jpabook.jpashop;

import org.junit.jupiter.api.Test;

public class LombokTest {

    @Test
    void lombokTest() {
        Hello hello = new Hello();
        hello.setData("hello");
        System.out.println(hello.getData());
    }
}
