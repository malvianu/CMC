package com.cmcmarkets.cmcdevelopmenttask;

import java.util.TreeSet;

public class OrderBook {
    private TreeSet<OrderBookItem> buyOrderBook;
    private TreeSet<OrderBookItem> sellOrderBook;

    public OrderBook(TreeSet<OrderBookItem> buyOrderBook, TreeSet<OrderBookItem> sellOrderBook) {
        this.buyOrderBook = buyOrderBook;
        this.sellOrderBook = sellOrderBook;
    }

    public TreeSet<OrderBookItem> getBuyOrderBook() {
        return buyOrderBook;
    }

    public TreeSet<OrderBookItem> getSellOrderBook() {
        return sellOrderBook;
    }

    public void setBuyOrderBook(TreeSet<OrderBookItem> buyOrderBook) {
        this.buyOrderBook = buyOrderBook;
    }

    public void setSellOrderBook(TreeSet<OrderBookItem> sellOrderBook) {
        this.sellOrderBook = sellOrderBook;
    }
}
