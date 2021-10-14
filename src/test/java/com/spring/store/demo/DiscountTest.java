package com.spring.store.demo;

import com.spring.store.demo.helper.DiscountHelper;
import com.spring.store.demo.models.*;
import com.spring.store.demo.services.DiscountService;
import com.spring.store.demo.services.DiscountServiceImpl;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiscountTest {

    @Test
    public void testCalculateTotal_GroceriesOnly() {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(ItemType.GROCERY, new BigDecimal(100.0)));
        items.add(new Item(ItemType.GROCERY, new BigDecimal(100.0)));
        items.add(new Item(ItemType.GROCERY, new BigDecimal(100.0)));

        DiscountHelper helper = new DiscountHelper();
        BigDecimal total = helper.calculateTotalPerType(items, ItemType.GROCERY);
        assertEquals(300.00, total.doubleValue(), 0);
    }

    @Test
    public void testCalculateTotalNonGroceriesOnly() {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(ItemType.CLOTHES, new BigDecimal(100.0)));
        items.add(new Item(ItemType.OTHER, new BigDecimal(100.0)));
        items.add(new Item(ItemType.TECHNOLOGY, new BigDecimal(100.0)));

        DiscountHelper helper = new DiscountHelper();
        BigDecimal total = helper.calculateTotal(items);
        assertEquals(300.00, total.doubleValue(), 0);
    }

    @Test
    public void testCalculateTotalMix() {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(ItemType.CLOTHES, new BigDecimal(100.0)));
        items.add(new Item(ItemType.OTHER, new BigDecimal(100.0)));
        items.add(new Item(ItemType.TECHNOLOGY, new BigDecimal(100.0)));
        items.add(new Item(ItemType.GROCERY, new BigDecimal(100.0)));
        items.add(new Item(ItemType.GROCERY, new BigDecimal(100.0)));

        DiscountHelper helper = new DiscountHelper();
        BigDecimal total = helper.calculateTotalPerType(items, ItemType.GROCERY);
        assertEquals(200.00, total.doubleValue(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testCalculateTotal_error() {
        DiscountHelper helper = new DiscountHelper();
        helper.getUserDiscount(null);
    }

    @Test
    public void testCalculateDiscount_10pct() {
        DiscountHelper helper = new DiscountHelper();
        BigDecimal total = helper.calculateDiscount(new BigDecimal(1000), new BigDecimal(0.1));
        assertEquals(900.00, total.doubleValue(), 0);
    }

    @Test
    public void testCalculateDiscount_50pct() {
        DiscountHelper helper = new DiscountHelper();
        BigDecimal total = helper.calculateDiscount(new BigDecimal(1000), new BigDecimal(0.5));
        assertEquals(500.00, total.doubleValue(), 0);
    }

    @Test
    public void testCalculateDiscount_0pct() {
        DiscountHelper helper = new DiscountHelper();
        BigDecimal total = helper.calculateDiscount(new BigDecimal(1000),  new BigDecimal(0.0));
        assertEquals(1000.00, total.doubleValue(), 0);
    }

    @Test
    public void testCalculateDiscount_100pct() {
        DiscountHelper helper = new DiscountHelper();
        BigDecimal total = helper.calculateDiscount(new BigDecimal(1000),  new BigDecimal(1.0));
        assertEquals(0.0, total.doubleValue(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDiscount_error() {
        DiscountHelper helper = new DiscountHelper();
        helper.calculateDiscount(new BigDecimal(1000),  new BigDecimal(2.0));
    }

    @Test
    public void testGetUserSpecificDiscount_affiliate() {
        User user = new User(UserType.AFFILIATE, LocalDate.now());
        DiscountHelper helper = new DiscountHelper();
        BigDecimal discount = helper.getUserDiscount(user);
        assertEquals(0.1, discount.doubleValue(), 0);
    }

    @Test
    public void testGetUserSpecificDiscount_employee() {
        User user = new User(UserType.EMPLOYEE, LocalDate.now());
        DiscountHelper helper = new DiscountHelper();
        BigDecimal discount = helper.getUserDiscount(user);
        assertEquals(0.3, discount.doubleValue(), 0);
    }

    @Test
    public void testGetUserSpecificDiscount_customer_old() {
        LocalDate joinDate = LocalDate.of(2016, 2, 23);
        User user = new User(UserType.CUSTOMER, joinDate);
        DiscountHelper helper = new DiscountHelper();
        BigDecimal discount = helper.getUserDiscount(user);
        assertEquals(0.05, discount.doubleValue(), 0);
    }

    @Test
    public void testGetUserSpecificDiscount_customer_new() {
        User user = new User(UserType.CUSTOMER, LocalDate.now());
        DiscountHelper helper = new DiscountHelper();
        BigDecimal discount = helper.getUserDiscount(user);
        assertEquals(0.0, discount.doubleValue(), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testGetUserSpecificDiscount_customer_null_user() {
        DiscountHelper helper = new DiscountHelper();
        helper.getUserDiscount(null);
    }

    @Test
    public void testIsCustomerSince() {
        DiscountHelper helper = new DiscountHelper();
        LocalDate joinDate = LocalDate.now();
        boolean isTwoYearsJoined = helper.isCustomerSince(joinDate, 2);
        assertFalse(isTwoYearsJoined);
    }

    @Test
    public void testIsCustomerSince_1year() {
        DiscountHelper helper = new DiscountHelper();
        LocalDate joinDate = LocalDate.now().minusYears(1);
        boolean isTwoYearsJoined = helper.isCustomerSince(joinDate, 2);
        assertFalse(isTwoYearsJoined);
    }

    @Test
    public void testIsCustomerSince_2years() {
        DiscountHelper helper = new DiscountHelper();
        LocalDate joinDate = LocalDate.now().minusYears(2);
        boolean isTwoYearsJoined = helper.isCustomerSince(joinDate, 2);
        assertTrue(isTwoYearsJoined);
    }

    @Test
    public void testIsCustomerSince_3years() {
        DiscountHelper helper = new DiscountHelper();
        LocalDate joinDate = LocalDate.now().minusYears(3);
        boolean isTwoYearsJoined = helper.isCustomerSince(joinDate, 2);
        assertTrue(isTwoYearsJoined);
    }

    @Test
    public void testCalculateBillsDiscount() {
        DiscountHelper helper = new DiscountHelper();
        BigDecimal amount = helper.calculateBillsDiscount(new BigDecimal(1000),  new BigDecimal(100),  new BigDecimal(5));
        assertEquals(50, amount.doubleValue(), 0);
    }

    @Test
    public void testCalculateBillsDiscount_2() {
        DiscountHelper helper = new DiscountHelper();
        BigDecimal amount = helper.calculateBillsDiscount(new BigDecimal(1000),  new BigDecimal(50),  new BigDecimal(5));
        assertEquals(100, amount.doubleValue(), 0);
    }

    @Test
    public void testCalculateBillsDiscount_3() {
        DiscountHelper helper = new DiscountHelper();
        BigDecimal amount = helper.calculateBillsDiscount( new BigDecimal(5632), new BigDecimal(100), new BigDecimal(5));
        assertEquals(280, amount.doubleValue(), 0);
    }

    @Test
	public void testDiscountServiceCalculate() {
		List<Item> items = new ArrayList<Item>();
		items.add(new Item(ItemType.GROCERY, new BigDecimal(50.0)));
		items.add(new Item(ItemType.TECHNOLOGY, new BigDecimal(200.0)));
		items.add(new Item(ItemType.GROCERY, new BigDecimal(10.0)));

		Bill bill = new Bill();
		bill.setItems(items);

		DiscountService discountService = new DiscountServiceImpl();

		discountService.discountCalculation(new User(UserType.CUSTOMER, LocalDate.now()), bill);
		DiscountHelper helper = new DiscountHelper();
		BigDecimal amount = helper.calculateBillsDiscount(new BigDecimal(5632), new BigDecimal(100), new BigDecimal(5));
		assertEquals(280, amount.doubleValue(), 0);
	}

}
