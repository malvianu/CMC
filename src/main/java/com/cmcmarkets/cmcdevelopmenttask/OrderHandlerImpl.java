package com.cmcmarkets.cmcdevelopmenttask;


import java.util.*;

public class OrderHandlerImpl implements OrderHandler{

    private final Set<Order> orders = new HashSet<>();
    private final Map<String, OrderBook> orderBooks = new HashMap<>();

    @Override
    public void addOrder(Order order) {
        orders.add(order);
        OrderBookHelper.updateOrderBook(order, orderBooks);
    }

    @Override
    public void modifyOrder(OrderModification orderModification) {
        Optional<Order> existingOrder = getExistingOrder(orderModification.getOrderId());
        if(existingOrder.isPresent()) {
            Order order = existingOrder.get();
            Order newOrder = new Order(order.getOrderId(), order.getSymbol(), order.getSide(), orderModification.getNewPrice(), orderModification.getNewQuantity());
            removeOrder(order.getOrderId());
            addOrder(newOrder);
        }
    }

    @Override
    public void removeOrder(long orderId) {
        Optional<Order> order = getExistingOrder(orderId);
        if(order.isPresent()) {
            orders.remove(order.get());
            OrderBookHelper.updateOrderBookForRemoval(order.get(), orderBooks);
        }
    }

    @Override
    public double getCurrentPrice(String symbol, int quantity, Side side) {
        SortedSet<OrderBookItem> orderBookItems = OrderBookHelper.getOrderBookItems(side, orderBooks.get(symbol));
        return OrderBookHelper.calculatePrice(quantity, orderBookItems);
    }

    private Optional<Order> getExistingOrder(long orderId) {
        return orders.stream().filter(order -> order.getOrderId() == orderId).findAny();
    }
}

