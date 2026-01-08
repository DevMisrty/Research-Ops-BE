package com.learning.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Orders {
    @Id
    private String id;

    private Double totalPrice;
    private Integer quantity;

    @Indexed
    private String status;

    @CreatedDate
    private LocalDateTime orderedAt;
}
