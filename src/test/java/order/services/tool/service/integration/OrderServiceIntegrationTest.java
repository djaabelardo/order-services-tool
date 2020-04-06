package order.services.tool.service.integration;

import static order.services.tool.utils.Constants.STATUS_OK;
import static order.services.tool.utils.Constants.STATUS_SUCCESS;
import static order.services.tool.utils.Constants.STATUS_TAKEN;
import static order.services.tool.utils.Constants.STATUS_UNASSIGNED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import order.services.tool.api.GoogleDistanceApiService;
import order.services.tool.api.model.GoogleDistanceResponse;
import order.services.tool.model.OrderDetail;
import order.services.tool.model.OrderLocationRequest;
import order.services.tool.model.OrderStatusRequest;
import order.services.tool.repository.OrderRepository;
import order.services.tool.service.OrderService;
    
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceIntegrationTest
{

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @Mock
    private GoogleDistanceApiService googleDistanceApiService;

    @Mock
    private GoogleDistanceResponse response;

    private static final String originLatitude = "40.6905615";
    private static final String originLongitude = "-73.9976592";
    private static final String destinationLatitude = "40.6905615";
    private static final String destinationLongitude = "-73.9976592";


    @Test
    public void shouldSuccessfullyCallRepository() throws IOException
    {
        OrderLocationRequest request = mock(OrderLocationRequest.class);
        when(request.getOrigin()).thenReturn(Lists.newArrayList(originLatitude, originLongitude));
        when(request.getDestination()).thenReturn(Lists.newArrayList(destinationLatitude, destinationLongitude));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(STATUS_OK);

        when(orderRepository.save(any(OrderDetail.class))).thenReturn(orderDetail);

        OrderDetail result = orderService.postOrder(request);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(STATUS_UNASSIGNED, result.getStatus());
    }

    
    @Test
    public void shouldSuccessfullyUpdateRepository() throws IOException
    {
        OrderStatusRequest request = mock(OrderStatusRequest.class);
        when(request.getStatus()).thenReturn(STATUS_TAKEN);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(STATUS_UNASSIGNED);

        when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(orderDetail));
        when(orderRepository.updateOrderStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(1);

        OrderDetail result = orderService.updateOrder(request, "id");

        assertNotNull(result);
        assertEquals(STATUS_SUCCESS, result.getStatus());
    }
   
    @Test
    public void shouldSuccessfullyRetrieveOrdersByPage()
    {
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        @SuppressWarnings("unchecked")
        Page<OrderDetail> orderDetailPage = mock(Page.class);
        OrderDetail order = new OrderDetail();
        when(orderDetailPage.getContent()).thenReturn(Lists.newArrayList(order));
        when(orderRepository.findAll(pageableCaptor.capture())).thenReturn(orderDetailPage);

        List<OrderDetail> orderDetail = orderService.getPaginatedOrders(2, 5);

        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        verify(orderRepository).findAll(pageableCaptor.capture());

        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertFalse(orderDetail.isEmpty());
    }

    
}
