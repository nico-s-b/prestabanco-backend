package com.example.tingeso1.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private String creditType;
    private int loanPeriod;
    private int creditMount;
    private int propertyValue;
    private float annualRate;
}
