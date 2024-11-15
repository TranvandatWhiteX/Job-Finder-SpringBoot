package com.dattran.job_finder_springboot.domain.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Salary {
    Long minSalary;

    Long maxSalary;

    String unit;

    CurrencyUnit currency;

    Type type;

    @Getter
    @AllArgsConstructor
    public enum CurrencyUnit {
        VND(100L),
        USD(101L),
        JPY(102L),
        GBP(103L),
        CNY(104L),;
        final Long currencyCode;
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        MONTHLY_WAGE, HOURLY_WAGE, YEARLY_WAGE
    }
}
