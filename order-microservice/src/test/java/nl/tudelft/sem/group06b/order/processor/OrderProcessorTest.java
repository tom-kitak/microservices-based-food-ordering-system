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
        order.setStatus(Status.ORDER_ONGOING);
        when(mockTimeValidation.isTimeValid(anyString(), anyInt())).thenReturn(true);
        when(mockOrderRepository.getOne(1L)).thenReturn(order);

        orderProcessor.setOrderTime(1L, "12:00");

        verify(mockOrderRepository, times(1)).save(any(Order.class));
    }
}
