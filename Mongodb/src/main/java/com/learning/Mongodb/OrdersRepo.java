package com.learning.mongodb;

import com.learning.mongodb.entity.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrdersRepo extends MongoRepository<Orders,String> {
}
