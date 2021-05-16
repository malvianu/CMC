package com.cmcmarkets.cmcdevelopmenttask;

import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

public class OrderBookItem {

    private int count;
    private int quantity;
    private int price;

    public OrderBookItem(int count, int quantity, int price) {
        this.count = count;
        this.quantity = quantity;
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderBookItem{" +
                "count=" + count +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
