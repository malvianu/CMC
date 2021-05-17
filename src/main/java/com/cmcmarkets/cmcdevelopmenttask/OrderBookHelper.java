package com.cmcmarkets.cmcdevelopmenttask;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public interface OrderBookHelper {

    final Comparator<OrderBookItem> sellOrderComparator = Comparator.comparingInt(OrderBookItem::getPrice);

    final Comparator<OrderBookItem> buyOrderComparator = Comparator.comparingInt(OrderBookItem::getPrice).reversed();

    public static void updateOrderBook(Order order, Map<String, OrderBook> orderBooks) {
        if(orderBooks.containsKey(order.getSymbol())) {
            SortedSet<OrderBookItem> orderBookItems;
                if(order.getSide().equals(Side.SELL)) {
                    orderBookItems = orderBooks.get(order.getSymbol()).getSellOrderBook();
                } else {
                    orderBookItems = orderBooks.get(order.getSymbol()).getBuyOrderBook();
                }

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

            OrderBookItem orderBookItem = new OrderBookItem(1, order.getQuantity(), order.getPrice());
            SortedSet<OrderBookItem> sellOrderBook = new TreeSet<>(sellOrderComparator);
            SortedSet<OrderBookItem> buyOrderBook = new TreeSet<>(buyOrderComparator);
            if(order.getSide().equals(Side.SELL)) {
               sellOrderBook.add(orderBookItem);
            } else {
               buyOrderBook.add(orderBookItem);
            }

            OrderBook newOrderBook = new OrderBook(buyOrderBook, sellOrderBook);
            orderBooks.put(order.getSymbol(),  newOrderBook);
        }
    }

     static void updateOrderBookForRemoval(Order order, Map<String, OrderBook> orderBooks) {

        OrderBook orderBook = orderBooks.get(order.getSymbol());

         SortedSet<OrderBookItem> orderBookItems;

        if(order.getSide() == Side.SELL) {
            orderBookItems = orderBook.getSellOrderBook();
        } else {
            orderBookItems = orderBook.getBuyOrderBook();
        }

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

    static Optional<OrderBookItem> findExistingOrderBookItem(Order order, SortedSet<OrderBookItem> orderBookItems) {
        return orderBookItems.stream().filter(orderBookItem -> orderBookItem.getPrice() == order.getPrice()).findAny();
    }
}
