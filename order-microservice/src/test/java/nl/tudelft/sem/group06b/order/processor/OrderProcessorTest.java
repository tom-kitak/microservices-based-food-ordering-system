package nl.tudelft.sem.group06b.order.processor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import nl.tudelft.sem.group06b.order.communication.CouponCommunication;
import nl.tudelft.sem.group06b.order.communication.MenuCommunication;
import nl.tudelft.sem.group06b.order.communication.StoreCommunication;
import nl.tudelft.sem.group06b.order.domain.Order;
import nl.tudelft.sem.group06b.order.domain.Status;
import nl.tudelft.sem.group06b.order.repository.OrderRepository;
import nl.tudelft.sem.group06b.order.service.processor.OrderProcessor;
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
    public void testSetOrderTimeInvalidTime() throws Exception {
        Order order = new Order();
        Long orderId = 1L;
        assertThrows(
                IllegalArgumentException.class,
                () -> orderProcessor.setOrderTime(orderId, ""), "Invalid time. The time should be at least 30 minutes after the current time");
        verify(mockOrderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testSetOrderTimeInvalidStatus() throws Exception {
        Order order = new Order();

        Long orderId = 1L;
        String time = "20/12/2022 16:11:30";

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
        when(mockOrderRepository.getOne(orderId)).thenReturn(order);

        orderProcessor.setOrderLocation("token", orderId, location);

        Order targetOrder = new Order();
        targetOrder.setId(orderId);
        targetOrder.setLocation(location);
        targetOrder.setStatus(Status.ORDER_ONGOING);
        targetOrder.setStoreId(storeId);

        verify(mockOrderRepository, times(1)).save(targetOrder);
    }


}
