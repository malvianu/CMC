package com.cmcmarkets.cmcdevelopmenttask;

import java.util.SortedSet;

public class OrderBook {
    private SortedSet<OrderBookItem> buyOrderBookItems;
    private SortedSet<OrderBookItem> sellOrderBookItems;

    public OrderBook(SortedSet<OrderBookItem> buyOrderBookItems, SortedSet<OrderBookItem> sellOrderBookItems) {
        this.buyOrderBookItems = buyOrderBookItems;
        this.sellOrderBookItems = sellOrderBookItems;
    }

    public SortedSet<OrderBookItem> getBuyOrderBookItems() {
        return buyOrderBookItems;
    }

    public SortedSet<OrderBookItem> getSellOrderBookItems() {
        return sellOrderBookItems;
    }

}
