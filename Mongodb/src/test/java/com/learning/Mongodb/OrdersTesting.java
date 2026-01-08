package com.learning.mongodb;

import com.learning.mongodb.entity.Orders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrdersTests {

    @Autowired
    private OrdersRepo ordersRepo;

    @Test
    void testSaveOrder() {
        Orders orders = Orders.builder()
                .quantity(2)
                .status("Active")
                .totalPrice(400D)
                .build();
        Orders res = ordersRepo.save(orders);

        System.out.println(res);


        assertEquals(orders, res);
        assertEquals(orders.getQuantity(), res.getQuantity());
        assertEquals(orders.getTotalPrice(), res.getTotalPrice());
    }
}
