package nl.tudelft.sem.group06b.order.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Pizza;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.model.ApplyCouponsToOrderModel;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.service.processor.OrderProcessor;
import nl.tudelft.sem.group06b.order.service.processor.OrderProcessorImpl;
import nl.tudelft.sem.group06b.order.util.TimeValidation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.TaskScheduler;

@SpringBootTest
public class OrderProcessorTest {
    @MockBean
    private OrderRepository mockOrderRepository;

    @MockBean
    private MenuCommunication mockMenuCommunication;

    @MockBean
    private StoreCommunication mockStoreCommunication;

    @MockBean
    private CouponCommunication mockCouponCommunication;

    @MockBean
    private TaskScheduler mockTaskScheduler;

    @MockBean
    private TimeValidation mockTimeValidation;

    @Autowired
    private OrderProcessor orderProcessor;

    @Test
    public void testConstruction() {
        Order order1 = new Order();
        order1.setStatus(Status.ORDER_PLACED);
        order1.setSelectedTime("11/11/2029 11:11:11");
        order1.setId(1L);
        Order order2 = new Order();
        order2.setStatus(Status.ORDER_FINISHED);
        order2.setSelectedTime("11/11/2019 11:11:11");
        order2.setId(2L);
        List<Order> orders = List.of(order1, order2);
        when(mockOrderRepository.findAll()).thenReturn(orders);
        when(mockOrderRepository.getOne(1L)).thenReturn(order1);

        OrderProcessor testOrderProcessor = new OrderProcessorImpl(
                mockOrderRepository, mockTaskScheduler, mockMenuCommunication,
                mockStoreCommunication, mockCouponCommunication, mockTimeValidation);

        verify(mockOrderRepository, times(1)).getOne(1L);
        verify(mockTaskScheduler, times(1)).schedule(any(), (Date) any());
    }

    @Test
    public void testStartOrderCorrect() throws Exception {
        Long ret = orderProcessor.startOrder("user");
        verify(mockOrderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testStartOrderIncorrect() throws Exception {
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.startOrder(""), "Invalid member ID");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderTimeCorrect() throws Exception {
        Order order = new Order();

        Long orderId = 1L;
        String selectedTime = "20/12/2022 16:11:30";

        order.setStatus(Status.ORDER_ONGOING);
        order.setId(orderId);

        when(mockTimeValidation.isTimeValid(selectedTime, 30)).thenReturn(true);
        when(mockOrderRepository.getOne(1L)).thenReturn(order);

        orderProcessor.setOrderTime(1L, selectedTime);

        Order targetOrder = new Order();
        targetOrder.setStatus(Status.ORDER_ONGOING);
        targetOrder.setId(orderId);
        targetOrder.setSelectedTime(selectedTime);

        verify(mockOrderRepository, times(1)).save(targetOrder);
    }

    @Test
    public void testSetOrderTimeOrderIdException() throws Exception {
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderTime(null, "20/12/2022 16:11:30"), "Invalid order ID");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderTimeInvalidTime() {
        Long orderId = 1L;
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderTime(orderId, ""),
                "Invalid time. The time should be at least 30 minutes after the current time");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderTimeBefore() throws Exception {
        Long orderId = 1L;
        when(mockTimeValidation.isTimeValid("11", 30)).thenReturn(false);
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderTime(orderId, "11"),
                "Invalid time. The time should be at least 30 minutes after the current time");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderTimeInvalidStatus() throws Exception {
        Order order = new Order();

        final Long orderId = 1L;
        final String time = "20/12/2022 16:11:30";

        order.setStatus(Status.ORDER_PLACED);
        order.setId(orderId);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);
        when(mockTimeValidation.isTimeValid(time, 30)).thenReturn(true);
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderTime(orderId, time), "No active order with this ID");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderLocationCorrect() throws Exception {
        Order order = new Order();

        Long orderId = 1L;
        Long storeId = 10L;
        String location = "Drebbelweg 40";

        order.setId(orderId);
        order.setStatus(Status.ORDER_ONGOING);

        when(mockStoreCommunication.getStoreIdFromLocation(location, "token")).thenReturn(storeId);
        when(mockStoreCommunication.validateLocation(location, "token")).thenReturn(true);
        when(mockOrderRepository.getOne(orderId)).thenReturn(order);

        orderProcessor.setOrderLocation("token", orderId, location);

        Order targetOrder = new Order();
        targetOrder.setId(orderId);
        targetOrder.setLocation(location);
        targetOrder.setStatus(Status.ORDER_ONGOING);
        targetOrder.setStoreId(storeId);

        verify(mockOrderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testSetOrderLocationEmptyToken() {
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderLocation("", 1L, "Drebbelweg 40"),
                "Invalid token");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderLocationInvalidEmptyLocationException() throws Exception {
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderLocation("token", 1L, ""), "Invalid store location");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderLocationInvalidLocationException() throws Exception {

        when(mockStoreCommunication.validateLocation("not valid location", "token")).thenReturn(false);
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderLocation("token", 1L, "not valid location"), "Invalid store location");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderLocationInvalidOrderStage() throws Exception {
        Order order = new Order();

        String location = "location";
        Long id = 1L;

        order.setId(id);
        order.setStatus(Status.ORDER_FINISHED);

        when(mockStoreCommunication.validateLocation(location, "token")).thenReturn(true);
        when(mockStoreCommunication.getStoreIdFromLocation(location, "token")).thenReturn(10L);
        when(mockOrderRepository.getOne(id)).thenReturn(order);

        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderLocation("token", id, location), "No active order with this ID");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testPlaceOrderInvalidToken() throws Exception {
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.placeOrder("", 1L), "Invalid token");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testPlaceOrderInvalidOrderId() throws Exception {
        assertThrows(
                NullPointerException.class,
                () -> orderProcessor.placeOrder("token", null), "Invalid order ID");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testPlaceOrderNoPizzasException() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);

        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.placeOrder("token", orderId), "Invalid order contents");
        verify(mockOrderRepository, never()).save(any(Order.class));

        order.setPizzas(List.of());
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.placeOrder("token", orderId), "Invalid order contents");
        verify(mockOrderRepository, never()).save(any(Order.class));

    }

    @Test
    public void testPlaceOrderNoSelectedTimeException() throws Exception {
        Long orderId = 1L;
        List<Pizza> pizzas = List.of(new Pizza(1L, List.of(10L, 20L), new BigDecimal(9.00)));

        Order order = new Order();
        order.setId(orderId);
        order.setPizzas(pizzas);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);

        assertThrows(
                UnsupportedOperationException.class,
                () -> orderProcessor.placeOrder("token", orderId), "No order time is selected");
        verify(mockOrderRepository, never()).save(any(Order.class));

        order.setSelectedTime("");
        assertThrows(
                UnsupportedOperationException.class,
                () -> orderProcessor.placeOrder("token", orderId), "No order time is selected");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testPlaceOrderInvalidStatusException() throws Exception {
        Long orderId = 1L;
        List<Pizza> pizzas = List.of(new Pizza(1L, List.of(10L, 20L), new BigDecimal(9.00)));
        String time = "20/12/2022 12:30:20";

        Order order = new Order();
        order.setId(orderId);
        order.setPizzas(pizzas);
        order.setSelectedTime(time);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);

        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.placeOrder("token", orderId), "No active order with this ID");
        verify(mockOrderRepository, never()).save(any(Order.class));

        order.setStatus(Status.ORDER_PLACED);
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.placeOrder("token", orderId), "No active order with this ID");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testPlaceOrderInvalidLocationException() throws Exception {
        Long orderId = 1L;
        List<Pizza> pizzas = List.of(new Pizza(1L, List.of(10L, 20L), new BigDecimal(9.00)));
        String time = "20/12/2022 12:30:20";
        Status status = Status.ORDER_ONGOING;

        Order order = new Order();
        order.setId(orderId);
        order.setPizzas(pizzas);
        order.setSelectedTime(time);
        order.setStatus(status);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);

        assertThrows(
                UnsupportedOperationException.class,
                () -> orderProcessor.placeOrder("token", orderId), "No store location is selected");
        verify(mockOrderRepository, never()).save(any(Order.class));

        order.setLocation("");
        assertThrows(
                UnsupportedOperationException.class,
                () -> orderProcessor.placeOrder("token", orderId), "No store location is selected");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testFetchOrderCorrect() throws Exception {
        Order order = new Order();
        when(mockOrderRepository.existsById(1L)).thenReturn(true);
        when(mockOrderRepository.getOne(1L)).thenReturn(order);

        assertEquals(order, orderProcessor.fetchOrder(1L));
        verify(mockOrderRepository, times(1)).getOne(1L);
    }

    @Test
    public void testFetchOrderNonExistent() {
        when(mockOrderRepository.existsById(1L)).thenReturn(false);
        assertThrows(
                Exception.class,
                () -> orderProcessor.fetchOrder(1L), "Invalid order ID");
        verify(mockOrderRepository, never()).getOne(1L);
    }

    @Test
    public void testFetchOrderEmptyId() {
        assertThrows(
                Exception.class,
                () -> orderProcessor.fetchOrder(null), "Invalid order ID");
        verify(mockOrderRepository, never()).getOne(1L);
    }

    @Test
    public void testFetchAllStoreOrdersAsRegional() throws Exception {
        Order order1 = new Order();
        order1.setStoreId(1L);
        order1.setId(1L);
        Order order2 = new Order();
        order2.setStoreId(2L);
        order2.setId(2L);
        Order order3 = new Order();
        order3.setStoreId(1L);
        order3.setId(3L);
        List<Order> orders = List.of(order1, order2, order3);

        when(mockOrderRepository.findAll()).thenReturn(orders);
        when(mockStoreCommunication.validateManager("man", "token")).thenReturn(false);

        Collection<Order> res = orderProcessor.fetchAllStoreOrders("token", "man", "regional_manager", 1L);
        assertThat(res).containsAll(List.of(order1, order3));

    }

    @Test
    public void testFetchAllStoreOrdersAsStore() throws Exception {
        Order order1 = new Order();
        order1.setStoreId(1L);
        order1.setId(1L);
        Order order2 = new Order();
        order2.setStoreId(2L);
        order2.setId(2L);
        Order order3 = new Order();
        order3.setStoreId(1L);
        order3.setId(3L);
        List<Order> orders = List.of(order1, order2, order3);

        when(mockOrderRepository.findAll()).thenReturn(orders);
        when(mockStoreCommunication.validateManager("man", "token")).thenReturn(true);
        when(mockStoreCommunication.getStoreIdFromManager("man", "token")).thenReturn(1L);

        Collection<Order> res = orderProcessor.fetchAllStoreOrders("token", "man", "customer", 1L);
        assertThat(res).containsAll(List.of(order1, order3));
    }

    @Test
    public void testFetchAllStoreOrdersAsWrongStore() throws Exception {
        Order order1 = new Order();
        order1.setStoreId(1L);
        order1.setId(1L);
        Order order2 = new Order();
        order2.setStoreId(2L);
        order2.setId(2L);
        Order order3 = new Order();
        order3.setStoreId(1L);
        order3.setId(3L);
        List<Order> orders = List.of(order1, order2, order3);

        when(mockOrderRepository.findAll()).thenReturn(orders);
        when(mockStoreCommunication.validateManager("man", "token")).thenReturn(true);
        when(mockStoreCommunication.getStoreIdFromManager("man", "token")).thenReturn(2L);

        assertThrows(UnsupportedOperationException.class, () -> orderProcessor
                .fetchAllStoreOrders(
                        "token",
                        "man",
                        "customer",
                        1L), "Not the store manager of this store");
    }

    @Test
    public void testFetchAllStoreOrdersAsCustomer() {
        Order order1 = new Order();
        order1.setStoreId(1L);
        order1.setId(1L);
        Order order2 = new Order();
        order2.setStoreId(2L);
        order2.setId(2L);
        Order order3 = new Order();
        order3.setStoreId(1L);
        order3.setId(3L);
        List<Order> orders = List.of(order1, order2, order3);

        when(mockOrderRepository.findAll()).thenReturn(orders);
        when(mockStoreCommunication.validateManager("man", "token")).thenReturn(false);

        assertThrows(Exception.class, () -> orderProcessor
                        .fetchAllStoreOrders("token", "man", "customer", 1L),
                "Customers can not view store orders");
    }

    @Test
    public void testFetchAllOrdersAsRegional() throws Exception {
        Order order1 = new Order();
        order1.setStoreId(1L);
        order1.setId(1L);
        Order order2 = new Order();
        order2.setStoreId(2L);
        order2.setId(2L);
        Order order3 = new Order();
        order3.setStoreId(1L);
        order3.setId(3L);
        List<Order> orders = List.of(order1, order2, order3);

        when(mockOrderRepository.findAll()).thenReturn(orders);
        assertThat(orderProcessor.fetchAllOrders("token", "deity", "regional_manager")).containsAll(orders);

    }

    @Test
    public void testFetchAllOrdersAsCustomer() {
        assertThrows(Exception.class, () -> orderProcessor
                        .fetchAllOrders("token", "deity", "customer"),
                "Only regional managers can view all orders");
    }

    @Test
    public void testCancelOrderEmptyOrderId() {
        assertThrows(IllegalArgumentException.class, () -> orderProcessor
                        .cancelOrder("token", "deity", "regional_manager", null),
                "Invalid order ID");
    }

    @Test
    public void testCancelOrderSuccessRegionalPlaced() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_PLACED);

        Order order2 = new Order();
        order2.setId(1L);
        order2.setStoreId(1L);
        order2.setStatus(Status.ORDER_CANCELED);

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("deity", "token")).thenReturn(false);
        orderProcessor.cancelOrder("token", "deity", "regional_manager", 1L);
        verify(mockOrderRepository).save(order2);
        verify(mockStoreCommunication).sendEmailToStore(1L, "Order with ID 1 cancelled", "token");
    }

    @Test
    public void testCancelOrderSuccessRegionalOngoing() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_ONGOING);

        Order order2 = new Order();
        order2.setId(1L);
        order2.setStoreId(1L);
        order2.setStatus(Status.ORDER_CANCELED);

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("deity", "token")).thenReturn(false);
        orderProcessor.cancelOrder("token", "deity", "regional_manager", 1L);
        verify(mockOrderRepository).save(order2);
        verify(mockStoreCommunication).sendEmailToStore(1L, "Order with ID 1 cancelled", "token");
    }

    @Test
    public void testCancelOrderExceptionRegional() {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_CANCELED);

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("deity", "token")).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> orderProcessor
                        .cancelOrder("token", "deity", "regional_manager", 1L),
                "No active order with this ID");

        verify(mockOrderRepository, never()).save(any());
        verify(mockStoreCommunication, never()).sendEmailToStore(anyLong(), anyString(), anyString());
    }

    @Test
    public void testCancelOrderSuccessStorePlaced() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_PLACED);

        Order order2 = new Order();
        order2.setId(1L);
        order2.setStoreId(1L);
        order2.setStatus(Status.ORDER_CANCELED);

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(true);
        when(mockStoreCommunication.getStoreIdFromManager("test", "token")).thenReturn(1L);
        orderProcessor.cancelOrder("token", "test", "customer", 1L);
        verify(mockOrderRepository).save(order2);
        verify(mockStoreCommunication).sendEmailToStore(1L, "Order with ID 1 cancelled", "token");
    }

    @Test
    public void testCancelOrderSuccessStoreOngoing() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_ONGOING);

        Order order2 = new Order();
        order2.setId(1L);
        order2.setStoreId(1L);
        order2.setStatus(Status.ORDER_CANCELED);

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(true);
        when(mockStoreCommunication.getStoreIdFromManager("test", "token")).thenReturn(1L);
        orderProcessor.cancelOrder("token", "test", "customer", 1L);
        verify(mockOrderRepository).save(order2);
        verify(mockStoreCommunication).sendEmailToStore(1L, "Order with ID 1 cancelled", "token");
    }

    @Test
    public void testCancelOrderStoreWrongStore() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_ONGOING);

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(true);
        when(mockStoreCommunication.getStoreIdFromManager("test", "token")).thenReturn(2L);
        assertThrows(UnsupportedOperationException.class,
                () -> orderProcessor.cancelOrder("token", "test", "customer", 1L),
                "Not the store manager of this store");
        verify(mockOrderRepository, never()).save(any());
        verify(mockStoreCommunication, never()).sendEmailToStore(anyLong(), anyString(), anyString());
    }

    @Test
    public void testCancelOrderStoreNoOngoing() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_FINISHED);

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(true);
        when(mockStoreCommunication.getStoreIdFromManager("test", "token")).thenReturn(1L);
        assertThrows(IllegalArgumentException.class,
                () -> orderProcessor.cancelOrder("token", "test", "customer", 1L),
                "No active order with this ID");
        verify(mockOrderRepository, never()).save(any());
        verify(mockStoreCommunication, never()).sendEmailToStore(anyLong(), anyString(), anyString());
    }

    @Test
    public void testCancelOrderCorrectCustomer() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_ONGOING);
        order.setMemberId("test");

        Order order2 = new Order();
        order2.setId(1L);
        order2.setStoreId(1L);
        order2.setStatus(Status.ORDER_CANCELED);
        order2.setMemberId("test");

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(false);
        when(mockTimeValidation.isTimeValid(any(), anyInt())).thenReturn(true);
        orderProcessor.cancelOrder("token", "test", "customer", 1L);
        verify(mockOrderRepository).save(order2);
        verify(mockStoreCommunication).sendEmailToStore(1L, "Order with ID 1 cancelled", "token");
    }

    @Test
    public void testCancelOrderInvalidTimeCustomer() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_ONGOING);
        order.setMemberId("test");

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(false);
        when(mockTimeValidation.isTimeValid(any(), anyInt())).thenReturn(false);
        assertThrows(Exception.class,
                () -> orderProcessor.cancelOrder("token", "test", "customer", 1L),
                "You can no longer cancel the order");

        verify(mockOrderRepository, never()).save(any());
        verify(mockStoreCommunication, never()).sendEmailToStore(anyLong(), anyString(), anyString());
    }

    @Test
    public void testCancelOrderInvalidStatusCustomer() {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_PLACED);
        order.setMemberId("test");

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> orderProcessor.cancelOrder("token", "test", "customer", 1L),
                "No active order with this ID");

        verify(mockOrderRepository, never()).save(any());
        verify(mockStoreCommunication, never()).sendEmailToStore(anyLong(), anyString(), anyString());
    }

    @Test
    public void testCancelOrderWrongCustomer() {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_PLACED);
        order.setMemberId("hmmm");

        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(false);
        assertThrows(UnsupportedOperationException.class,
                () -> orderProcessor.cancelOrder("token", "test", "customer", 1L),
                "Access denied.");

        verify(mockOrderRepository, never()).save(any());
        verify(mockStoreCommunication, never()).sendEmailToStore(anyLong(), anyString(), anyString());
    }

    @Test
    public void testCancelOrderIncorrectRole() {
        Order order = new Order();
        order.setId(1L);
        order.setStoreId(1L);
        order.setStatus(Status.ORDER_PLACED);
        order.setMemberId("hmmm");
        when(mockOrderRepository.getOne(1L)).thenReturn(order);
        when(mockStoreCommunication.validateManager("test", "token")).thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> orderProcessor.cancelOrder("token", "test", "brainbrain", 1L),
                "Role is not handled");
        verify(mockOrderRepository, never()).save(any());
        verify(mockStoreCommunication, never()).sendEmailToStore(anyLong(), anyString(), anyString());
    }

    @Test
    public void testPlaceOrderSuccessNoCoupons() throws Exception {
        Long orderId = 1L;
        Pizza pizza1 = new Pizza(1L, List.of(10L, 20L));
        Pizza pizza2 = new Pizza(2L, List.of(20L));
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        String time = "20/12/2022 12:30:20";
        Status status = Status.ORDER_ONGOING;
        Long storeId = 2L;
        String location = "Valid location 49";

        Order order = new Order();
        order.setId(orderId);
        order.setPizzas(pizzas);
        order.setSelectedTime(time);
        order.setStatus(status);
        order.setStoreId(storeId);
        order.setLocation(location);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);
        when(mockMenuCommunication.getPizzaPriceFromMenu(pizza1, "token")).thenReturn(new BigDecimal("9.00"));
        when(mockMenuCommunication.getPizzaPriceFromMenu(pizza2, "token")).thenReturn(new BigDecimal("10.42"));

        Order target = new Order();
        target.setId(orderId);
        Pizza pizza1Target = new Pizza(1L, List.of(10L, 20L), new BigDecimal("9.00").setScale(2, RoundingMode.HALF_UP));
        Pizza pizza2Target = new Pizza(2L, List.of(20L), new BigDecimal("10.42").setScale(2, RoundingMode.HALF_UP));
        List<Pizza> pizzasTarget = List.of(pizza1Target, pizza2Target);
        target.setPizzas(pizzasTarget);
        target.setSelectedTime(time);
        target.setStatus(Status.ORDER_PLACED);
        target.setPrice(target.calculateTotalPrice());
        target.setStoreId(storeId);
        target.setLocation(location);

        Order placed = orderProcessor.placeOrder("token", orderId);
        assert placed.getPrice().compareTo(new BigDecimal("19.42").setScale(2, RoundingMode.HALF_UP)) == 0;
        assert placed.equals(target);

        verify(mockOrderRepository, times(1)).save(target);
    }

    @Test
    public void testPlaceOrderSuccessWithCoupons() throws Exception {
        Long orderId = 1L;
        Pizza pizza1 = new Pizza(1L, List.of(10L, 20L));
        Pizza pizza2 = new Pizza(2L, List.of(20L));
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        String time = "20/12/2022 12:30:20";
        Status status = Status.ORDER_ONGOING;
        Long storeId = 2L;
        String location = "Valid location 49";
        Set<String> coupons = Set.of("c1", "oneFree");

        Order order = new Order();
        order.setId(orderId);
        order.setPizzas(pizzas);
        order.setSelectedTime(time);
        order.setStatus(status);
        order.setStoreId(storeId);
        order.setLocation(location);
        order.setCoupons(coupons);

        Pizza pizza1Target = new Pizza(1L, List.of(10L, 20L), new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP));
        Pizza pizza2Target = new Pizza(2L, List.of(20L), new BigDecimal("10.42").setScale(2, RoundingMode.HALF_UP));
        List<Pizza> pizzasTarget = List.of(pizza1Target, pizza2Target);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);
        when(mockMenuCommunication.getPizzaPriceFromMenu(pizza1, "token"))
                .thenReturn(new BigDecimal("9.00").setScale(2, RoundingMode.HALF_UP));
        when(mockMenuCommunication.getPizzaPriceFromMenu(pizza2, "token"))
                .thenReturn(new BigDecimal("10.42").setScale(2, RoundingMode.HALF_UP));
        when(mockCouponCommunication.applyCouponsToOrder(order.getPizzas(),
                new ArrayList<>(order.getCoupons()), "token")).thenReturn(
                new ApplyCouponsToOrderModel(pizzasTarget, List.of("oneFree")));


        Order target = new Order();
        target.setId(orderId);
        target.setPizzas(pizzasTarget);
        target.setSelectedTime(time);
        target.setStatus(Status.ORDER_PLACED);
        target.setPrice(new BigDecimal("10.42").setScale(2, RoundingMode.HALF_UP));
        target.setStoreId(storeId);
        target.setLocation(location);
        target.setCoupons(coupons);

        Order placed = orderProcessor.placeOrder("token", orderId);
        assert placed.getPrice().compareTo(new BigDecimal("10.42").setScale(2, RoundingMode.HALF_UP)) == 0;
        assert placed.equals(target);

        verify(mockOrderRepository, times(1)).save(target);
    }

    @Test
    public void testPlaceOrderSuccessWithNoValidCoupons() throws Exception {
        Long orderId = 1L;
        Pizza pizza1 = new Pizza(1L, List.of(10L, 20L));
        Pizza pizza2 = new Pizza(2L, List.of(20L));
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        String time = "20/12/2022 12:30:20";
        Status status = Status.ORDER_ONGOING;
        Long storeId = 2L;
        String location = "Valid location 49";
        Set<String> coupons = Set.of("notValid2", "notValid1");

        Order order = new Order();
        order.setId(orderId);
        order.setPizzas(pizzas);
        order.setSelectedTime(time);
        order.setStatus(status);
        order.setStoreId(storeId);
        order.setLocation(location);
        order.setCoupons(coupons);

        Pizza pizza1Target = new Pizza(1L, List.of(10L, 20L), new BigDecimal("9.00").setScale(2, RoundingMode.HALF_UP));
        Pizza pizza2Target = new Pizza(2L, List.of(20L), new BigDecimal("10.42").setScale(2, RoundingMode.HALF_UP));
        List<Pizza> pizzasTarget = List.of(pizza1Target, pizza2Target);

        when(mockOrderRepository.getOne(orderId)).thenReturn(order);
        when(mockMenuCommunication.getPizzaPriceFromMenu(pizza1, "token"))
                .thenReturn(new BigDecimal("9.00").setScale(2, RoundingMode.HALF_UP));
        when(mockMenuCommunication.getPizzaPriceFromMenu(pizza2, "token"))
                .thenReturn(new BigDecimal("10.42").setScale(2, RoundingMode.HALF_UP));
        when(mockCouponCommunication.applyCouponsToOrder(order.getPizzas(),
                new ArrayList<>(order.getCoupons()), "token")).thenReturn(
                new ApplyCouponsToOrderModel(pizzasTarget, List.of()));

        Order target = new Order();
        target.setId(orderId);
        target.setPizzas(pizzasTarget);
        target.setSelectedTime(time);
        target.setStatus(Status.ORDER_PLACED);
        target.setPrice(new BigDecimal("19.42").setScale(2, RoundingMode.HALF_UP));
        target.setStoreId(storeId);
        target.setLocation(location);
        target.setCoupons(coupons);

        Order placed = orderProcessor.placeOrder("token", orderId);
        assert placed.getPrice().compareTo(new BigDecimal("19.42").setScale(2, RoundingMode.HALF_UP)) == 0;
        assert placed.equals(target);

        verify(mockOrderRepository, times(1)).save(target);
    }


}
