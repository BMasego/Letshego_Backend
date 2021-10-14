package com.spring.store.demo.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.spring.store.demo.models.Item;
import com.spring.store.demo.models.ItemType;
import com.spring.store.demo.models.User;
import com.spring.store.demo.models.UserType;

public class DiscountHelper {

    private static final int YEARS_FOR_DISCOUNT = 2;

    private static final double EMPLOYEE_DISCOUNT_PERCENTAGE = 0.30;
    private static final double AFFILIATE_DISCOUNT_PERCENTAGE = 0.10;
    private static final double CUSTOMER_DISCOUNT_PERCENTAGE = 0.05;


    public BigDecimal calculateTotal(List<Item> items) {
        return items.stream().map(i -> i.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalPerType(List<Item> items, ItemType type) {
        BigDecimal sum = new BigDecimal(0);

        if (type != null) {
            sum = items.stream().filter(i -> type.equals(i.getType())).map(i -> i.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return sum;
    }

    public BigDecimal getUserDiscount(User user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null");
        }

        BigDecimal discount = new BigDecimal(0);

        UserType type = user.getType();

        switch (type) {
            case EMPLOYEE:
                discount = new BigDecimal(EMPLOYEE_DISCOUNT_PERCENTAGE).setScale(2, RoundingMode.HALF_EVEN);
                break;

            case AFFILIATE:
                discount = new BigDecimal(AFFILIATE_DISCOUNT_PERCENTAGE).setScale(2, RoundingMode.HALF_EVEN);
                break;

            case CUSTOMER:
                if (isCustomerSince(user.getRegisterDate(), YEARS_FOR_DISCOUNT)) {
                    discount = new BigDecimal(CUSTOMER_DISCOUNT_PERCENTAGE).setScale(2, RoundingMode.HALF_EVEN);
                }
                break;

            default:
                break;
        }

        return discount;
    }

    public boolean isCustomerSince(LocalDate registeredDate, long years) {
        Period period = Period.between(registeredDate, LocalDate.now());
        return period.getYears() >= years;
    }

    public BigDecimal calculateBillsDiscount(BigDecimal totalAmount, BigDecimal amount, BigDecimal discountAmount) {
        int value = totalAmount.divide(amount).intValue();
        return discountAmount.multiply(new BigDecimal(value));
    }

    public BigDecimal calculateDiscount(BigDecimal amount, BigDecimal discount) {
        if (discount.doubleValue() > 1.0) {
            throw new IllegalArgumentException("Discount cannot be more than 100%");
        }

        BigDecimal x = amount.multiply(discount);
        return amount.subtract(x);
    }

}
