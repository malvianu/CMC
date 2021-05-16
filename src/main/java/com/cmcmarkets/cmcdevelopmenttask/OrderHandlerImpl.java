package com.cmcmarkets.cmcdevelopmenttask;

import java.util.*;


public class OrderHandlerImpl implements OrderHandler{
    private final Comparator<OrderBookItem> sellOrderComparator = (OrderBookItem item1, OrderBookItem item2) -> { if(item1.getPrice() == item2.getPrice()) {
        return 0;
    } else {
        return  item1.getPrice() < item2.getPrice() ? -1:1;
    }};
    private final Comparator<OrderBookItem> buyOrderComparator = (OrderBookItem item1, OrderBookItem item2) -> { if(item1.getPrice() == item2.getPrice()) {
        return 0;
    } else {
        return  item1.getPrice() < item2.getPrice() ? 1:-1;
    }};
    private final Set<Order> orders = new HashSet<>();
    private final Map<String, OrderBook> orderBooks = new HashMap<>();



    @Override
    public void addOrder(Order order) {
        orders.add(order);
        updateOrderBook(order);
    }

    @Override
    public void modifyOrder(OrderModification orderModification) {
        //assuming only existing orders will be modified as mentioned in the design document hence not doing optional isPresent() check
        Order existingOrder = orders.stream().filter(order -> order.getOrderId() == orderModification.getOrderId()).findAny().get();
        Order newOrder = new Order(existingOrder.getOrderId(), existingOrder.getSymbol(), existingOrder.getSide(), orderModification.getNewPrice(), orderModification.getNewQuantity());
        removeOrder(existingOrder.getOrderId());
        addOrder(newOrder);
    }

    @Override
    public void removeOrder(long orderId) {
        Order order = orders.stream().filter(order1 -> order1.getOrderId() == orderId).findAny().get();
        orders.remove(order);
        updateOrderBookForRemoval(order);
    }

    @Override
    public double getCurrentPrice(String symbol, int quantity, Side side) {
        TreeSet<OrderBookItem> orderBookItems;
        if(side.equals(Side.SELL)) {
            orderBookItems = orderBooks.get(symbol).getSellOrderBook();
        } else {
            orderBookItems = orderBooks.get(symbol).getBuyOrderBook();
        }

        Double currentPrice = calculatePrice(quantity, orderBookItems);
        return currentPrice;
    }

    private Double calculatePrice(int quantity, TreeSet<OrderBookItem> orderBookItems) {
        double price = 0.0;
        int originalQuantity = quantity;
        for(OrderBookItem orderBookItem : orderBookItems) {
            if(quantity <= 0 ) {
                break;
            } else if(quantity <= orderBookItem.getQuantity()){
                price = price + orderBookItem.getPrice()*quantity;
                quantity = quantity - orderBookItem.getQuantity();
            } else {
                price = price + orderBookItem.getPrice()*orderBookItem.getQuantity();
                quantity = quantity - orderBookItem.getQuantity();
            }
            }
        return price/originalQuantity;
        }


    @Override
    public void printOrderBook(String symbol) {
        OrderBook orderBook = orderBooks.get(symbol);
        System.out.println("Printing sell book order");
        TreeSet<OrderBookItem> sellOrderBook = orderBook.getSellOrderBook();
        sellOrderBook.stream().forEach(orderBookItem -> System.out.println(orderBookItem.toString()));
        System.out.println("Printing buy book order");
        TreeSet<OrderBookItem> buyOrderBook = orderBook.getBuyOrderBook();
        buyOrderBook.stream().forEach(orderBookItem -> System.out.println(orderBookItem.toString()));


    }

    private void updateOrderBook(Order order) {
        if(orderBooks.containsKey(order.getSymbol())) {
            if(order.getSide().equals(Side.SELL)) {
                OrderBookItem orderBookItem = new OrderBookItem(1,order.getQuantity(), order.getPrice());
                TreeSet<OrderBookItem> sellOrderBook = orderBooks.get(order.getSymbol()).getSellOrderBook();
                Optional<OrderBookItem> existingOrderBookItem = sellOrderBook.stream().filter(orderBookItem1 -> orderBookItem1.getPrice()== order.getPrice()).findAny();
                if(existingOrderBookItem.isPresent()) {
                    OrderBookItem existingOrderBookItem1 = existingOrderBookItem.get();
                    existingOrderBookItem1.setCount(existingOrderBookItem1.getCount()+1);
                    existingOrderBookItem1.setQuantity(existingOrderBookItem1.getQuantity()+order.getQuantity());
                } else {
                    sellOrderBook.add(orderBookItem);
                }
            }

            if(order.getSide().equals(Side.BUY)) {
                OrderBookItem orderBookItem = new OrderBookItem(1,order.getQuantity(), order.getPrice());
                TreeSet<OrderBookItem> buyOrderBook = orderBooks.get(order.getSymbol()).getBuyOrderBook();
                Optional<OrderBookItem> existingOrderBookItem = buyOrderBook.stream().filter(orderBookItem1 -> orderBookItem1.getPrice()== order.getPrice()).findAny();
                if(existingOrderBookItem.isPresent()) {
                    OrderBookItem existingOrderBookItem1 = existingOrderBookItem.get();
                    existingOrderBookItem1.setCount(existingOrderBookItem1.getCount()+1);
                    existingOrderBookItem1.setQuantity(existingOrderBookItem1.getQuantity()+order.getQuantity());
                } else {
                    buyOrderBook.add(orderBookItem);
                }
            }
        } else {

            OrderBookItem orderBookItem = new OrderBookItem(1, order.getQuantity(), order.getPrice());
            TreeSet<OrderBookItem> sellOrderBook = new TreeSet<>(sellOrderComparator);
            TreeSet<OrderBookItem> buyOrderBook = new TreeSet<>(buyOrderComparator);
            if(order.getSide().equals(Side.SELL)) {
                sellOrderBook.add(orderBookItem);
            } else {
                buyOrderBook.add(orderBookItem);
            }

            OrderBook newOrderBook = new OrderBook(buyOrderBook, sellOrderBook);
            orderBooks.put(order.getSymbol(),  newOrderBook);
        }




    }

    private void updateOrderBookForRemoval(Order order) {

        OrderBook orderBook = orderBooks.get(order.getSymbol());

        TreeSet<OrderBookItem> orderBookItems;

        if(order.getSide() == Side.SELL) {
            orderBookItems = orderBook.getSellOrderBook();
        } else {
            orderBookItems = orderBook.getBuyOrderBook();
        }

        OrderBookItem existingOrderBookItem = orderBookItems.stream().filter(orderBookItem -> orderBookItem.getPrice() == order.getPrice()).findAny().get();
        if(existingOrderBookItem.getQuantity() == order.getQuantity()) {
            orderBookItems.remove(existingOrderBookItem);
        } else {
            existingOrderBookItem.setQuantity(existingOrderBookItem.getQuantity()-order.getQuantity());
            existingOrderBookItem.setCount(existingOrderBookItem.getCount()-1);
        }
    }


}

