package com.spring.store.demo.services;

import com.spring.store.demo.models.Bill;
import com.spring.store.demo.models.User;

import java.math.BigDecimal;

/**
 * This is the Discount interface file
 */
public interface DiscountService {

    /**
     * This method calculate discount for given user and bill
     * @param user  - User
     * @param bill - Bill
     * @return Double - payable amount for the user
     */
    BigDecimal discountCalculation(User user, Bill bill);
}
