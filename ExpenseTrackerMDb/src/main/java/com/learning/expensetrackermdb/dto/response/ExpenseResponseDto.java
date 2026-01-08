package com.learning.expensetrackermdb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ExpenseResponseDto {

    private String category;
    private double amount;
    private String note;
    private String date;
}
