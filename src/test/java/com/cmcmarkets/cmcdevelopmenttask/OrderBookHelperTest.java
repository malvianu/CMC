package com.cmcmarkets.cmcdevelopmenttask;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class OrderBookHelperTest {
    SortedSet<OrderBookItem> sellOrderBookItems;
    SortedSet<OrderBookItem> buyOrderBookItems;
    OrderBook orderBook ;
    Map<String, OrderBook> orderBooks ;

    @Before
    public void setUp() {
        initializeData();
    }

    @Test
    public void forExistingPriceNewBuyOrderShoudlUpdateTheQuantityForThePrice() {
        orderBooks.put("ABC", orderBook);
        Order order = new Order(3l,"ABC", Side.BUY, 40, 2);
        OrderBookHelper.updateOrderBook(order, orderBooks);

        Assert.assertEquals(24,orderBooks.get("ABC").getBuyOrderBookItems().stream().filter(orderBookItem -> orderBookItem.getPrice() == 40).findAny().get().getQuantity());
    }

    @Test
    public void forNewPriceBuyOrderNewPriceShouldBeAddedToOrderBook(){
        orderBooks.put("ABC", orderBook);
        Order newPriceOrder = new Order(4l,"ABC", Side.BUY, 10, 2);
        OrderBookHelper.updateOrderBook(newPriceOrder, orderBooks);

        Assert.assertEquals(5, orderBooks.get("ABC").getBuyOrderBookItems().size());
    }

    @Test
    public void forExistingPriceNewSellOrderTheQuantityShouldBeUpdatedForThePrice() {
        orderBooks.put("ABC", orderBook);
        Order order = new Order(3l,"ABC", Side.SELL, 40, 2);
        OrderBookHelper.updateOrderBook(order, orderBooks);

        Assert.assertEquals(24,orderBooks.get("ABC").getSellOrderBookItems().stream().filter(orderBookItem -> orderBookItem.getPrice() == 40).findAny().get().getQuantity());
    }

    @Test
    public void forNewPriceSellOrderNewPriceShouldBeAddedToOrderBook(){
        orderBooks.put("ABC", orderBook);
        Order newPriceOrder = new Order(4l,"ABC", Side.SELL, 10, 2);
        OrderBookHelper.updateOrderBook(newPriceOrder, orderBooks);

        Assert.assertEquals(5, orderBooks.get("ABC").getSellOrderBookItems().size());
    }

    @Test
    public void orderForNewSymbolShouldAddNewEntryInOrderBook() {
        orderBooks.put("ABC", orderBook);
        Order order = new Order(3l,"TEST", Side.BUY, 40, 2);
        OrderBookHelper.updateOrderBook(order, orderBooks);

        Assert.assertEquals(2, orderBooks.keySet().size());
    }

    @Test
    public void testUpdateOrderBookForRemoval() {
        orderBooks.put("ABC", orderBook);
        Order order = new Order(3l,"ABC", Side.SELL, 40, 2);
        OrderBookHelper.updateOrderBookForRemoval(order, orderBooks);

        Assert.assertEquals(20,orderBooks.get("ABC").getSellOrderBookItems().stream().filter(orderBookItem -> orderBookItem.getPrice() == 40).findAny().get().getQuantity());
    }

    @Test
    public void updateOrderBookForRemovalShouldRemovePriceIfRemovedOrderQuantityEqualOrderBookPrice() {
        orderBooks.put("ABC", orderBook);
        Order order = new Order(3l,"ABC", Side.SELL, 40, 22);
        OrderBookHelper.updateOrderBookForRemoval(order, orderBooks);

        Assert.assertEquals(3, orderBooks.get("ABC").getSellOrderBookItems().size());
        //Buy orderbook item should not be impacted
        Assert.assertEquals(4, orderBooks.get("ABC").getBuyOrderBookItems().size());
    }

    @Test
    public void testCalculatePriceForSaleOrder() {
        Assert.assertEquals(32.33,Math.round(OrderBookHelper.calculatePrice(60, sellOrderBookItems)*100.0)/100.0, 0.001);
        Assert.assertEquals(24.00,Math.round(OrderBookHelper.calculatePrice(30, sellOrderBookItems)*100.0)/100.0, 0.001);
    }

    @Test
    public void testCalculatePriceForBuyOrder() {
        Assert.assertEquals(50.00,Math.round(OrderBookHelper.calculatePrice(12, buyOrderBookItems)*100.0)/100.0, 0.001);
        Assert.assertEquals(45.00,Math.round(OrderBookHelper.calculatePrice(30, buyOrderBookItems)*100.0)/100.0, 0.001);
    }

    private void initializeData() {
        OrderBookItem orderBookItem1 = new OrderBookItem(1, 15, 50);
        OrderBookItem orderBookItem2 = new OrderBookItem(2, 18, 20);
        OrderBookItem orderBookItem3 = new OrderBookItem(2, 22, 40);
        OrderBookItem orderBookItem4 = new OrderBookItem(1, 15, 30);

        buyOrderBookItems = new TreeSet<>(OrderBookHelper.buyOrderComparator);
        buyOrderBookItems.add(orderBookItem1);
        buyOrderBookItems.add(orderBookItem2);
        buyOrderBookItems.add(orderBookItem3);
        buyOrderBookItems.add(orderBookItem4);

        sellOrderBookItems = new TreeSet<>(OrderBookHelper.sellOrderComparator);
        sellOrderBookItems.add(orderBookItem1);
        sellOrderBookItems.add(orderBookItem2);
        sellOrderBookItems.add(orderBookItem3);
        sellOrderBookItems.add(orderBookItem4);

        orderBook = new OrderBook(buyOrderBookItems, sellOrderBookItems);
        orderBooks = new HashMap<>();
    }
}
