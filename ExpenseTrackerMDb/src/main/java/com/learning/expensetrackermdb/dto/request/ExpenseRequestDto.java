package com.learning.expensetrackermdb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ExpenseRequestDto {

    private double amount;
    private String category;
    private String note;
    private List<String> tags;
}
