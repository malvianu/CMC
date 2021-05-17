package com.cmcmarkets.cmcdevelopmenttask;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public class OrderBookHelper {

    private OrderBookHelper() {
    }

    private static final Comparator<OrderBookItem> sellOrderComparator = Comparator.comparingInt(OrderBookItem::getPrice);

    private static final Comparator<OrderBookItem> buyOrderComparator = Comparator.comparingInt(OrderBookItem::getPrice).reversed();

     public static void updateOrderBook(Order order, Map<String, OrderBook> orderBooks) {
        if(orderBooks.containsKey(order.getSymbol())) {
            SortedSet<OrderBookItem> orderBookItems = getOrderBookItems(order.getSide(), orderBooks.get(order.getSymbol()));
            Optional<OrderBookItem> existingOrderBookItem = findExistingOrderBookItem(order, orderBookItems);

            if(existingOrderBookItem.isPresent()) {
                OrderBookItem existingOrderBookItem1 = existingOrderBookItem.get();
                existingOrderBookItem1.setCount(existingOrderBookItem1.getCount()+1);
                existingOrderBookItem1.setQuantity(existingOrderBookItem1.getQuantity()+order.getQuantity());
            } else {
                OrderBookItem orderBookItem = new OrderBookItem(1,order.getQuantity(), order.getPrice());
                orderBookItems.add(orderBookItem);
            }
        } else {
            OrderBook newOrderBook = initializeOrderBookForNewSymbol(order);
            orderBooks.put(order.getSymbol(),  newOrderBook);
        }
    }

    private static OrderBook initializeOrderBookForNewSymbol(Order order) {
        OrderBookItem orderBookItem = new OrderBookItem(1, order.getQuantity(), order.getPrice());
        SortedSet<OrderBookItem> sellOrderBook = new TreeSet<>(sellOrderComparator);
        SortedSet<OrderBookItem> buyOrderBook = new TreeSet<>(buyOrderComparator);
        if(order.getSide().equals(Side.SELL)) {
           sellOrderBook.add(orderBookItem);
        } else {
           buyOrderBook.add(orderBookItem);
        }

        return new OrderBook(buyOrderBook, sellOrderBook);
    }

    public static SortedSet<OrderBookItem> getOrderBookItems(Side side, OrderBook orderBook) {
        SortedSet<OrderBookItem> orderBookItems;
        if (side.equals(Side.SELL)) {
            orderBookItems = orderBook.getSellOrderBook();
        } else {
            orderBookItems = orderBook.getBuyOrderBook();
        }
        return orderBookItems;
    }

    public static void updateOrderBookForRemoval(Order order, Map<String, OrderBook> orderBooks) {
        SortedSet<OrderBookItem> orderBookItems = getOrderBookItems(order.getSide(), orderBooks.get(order.getSymbol()));

        Optional<OrderBookItem> existingOrderBookItem = findExistingOrderBookItem(order, orderBookItems);
        if(existingOrderBookItem.isPresent()) {
            OrderBookItem orderBookItem = existingOrderBookItem.get();
            if(orderBookItem.getQuantity() == order.getQuantity()) {
                orderBookItems.remove(orderBookItem);
            } else {
                orderBookItem.setQuantity(orderBookItem.getQuantity()-order.getQuantity());
                orderBookItem.setCount(orderBookItem.getCount()-1);
            }
        }

    }

    private static Optional<OrderBookItem> findExistingOrderBookItem(Order order, SortedSet<OrderBookItem> orderBookItems) {
        return orderBookItems.stream().filter(orderBookItem -> orderBookItem.getPrice() == order.getPrice()).findAny();
    }
}
