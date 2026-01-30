package com.practice.researchopsproject.entity;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
@Builder
public class BillingInfo {

    private String billingContact;
    private BillingMethod billingMethod;
}
