package com.cmcmarkets.cmcdevelopmenttask;

import java.util.SortedSet;

public class OrderBook {
    private SortedSet<OrderBookItem> buyOrderBook;
    private SortedSet<OrderBookItem> sellOrderBook;

    public OrderBook(SortedSet<OrderBookItem> buyOrderBook, SortedSet<OrderBookItem> sellOrderBook) {
        this.buyOrderBook = buyOrderBook;
        this.sellOrderBook = sellOrderBook;
    }

    public SortedSet<OrderBookItem> getBuyOrderBook() {
        return buyOrderBook;
    }

    public SortedSet<OrderBookItem> getSellOrderBook() {
        return sellOrderBook;
    }

    public void setBuyOrderBook(SortedSet<OrderBookItem> buyOrderBook) {
        this.buyOrderBook = buyOrderBook;
    }

    public void setSellOrderBook(SortedSet<OrderBookItem> sellOrderBook) {
        this.sellOrderBook = sellOrderBook;
    }
}
