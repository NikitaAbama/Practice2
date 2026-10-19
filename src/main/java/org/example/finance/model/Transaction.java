package org.example.finance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Одна банківська операція: дата, сума (дохід +, витрата -), категорія. */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Transaction {
    private String date;
    private double amount;
    private String description;
}
