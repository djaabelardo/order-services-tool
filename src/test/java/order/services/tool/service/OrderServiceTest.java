package order.services.tool.service;

import static order.services.tool.utils.Constants.STATUS_OK;
import static order.services.tool.utils.Constants.STATUS_SUCCESS;
import static order.services.tool.utils.Constants.STATUS_TAKEN;
import static order.services.tool.utils.Constants.STATUS_UNASSIGNED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Lists;

import order.services.tool.api.GoogleDistanceApiService;
import order.services.tool.api.model.Distance;
import order.services.tool.api.model.Element;
import order.services.tool.api.model.GoogleDistanceResponse;
import order.services.tool.api.model.Row;
import order.services.tool.exception.DataNotFoundException;
import order.services.tool.exception.DatabaseException;
import order.services.tool.model.OrderDetail;
import order.services.tool.model.OrderLocationRequest;
import order.services.tool.model.OrderStatusRequest;
import order.services.tool.repository.OrderRepository;

public class OrderServiceTest
{

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private GoogleDistanceApiService googleDistanceApiService;

    @Mock
    private GoogleDistanceResponse response;

    @Mock
    private Row row;

    @Mock
    private Element element;

    @Mock
    private Distance distance;

    @Mock
    private OrderDetail orderDetail;

    private static final String originLatitude = "40.6905615";
    private static final String originLongitude = "-73.9976592";
    private static final String destinationLatitude = "40.6905615";
    private static final String destinationLongitude = "-73.9976592";

    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSuccessfullyCallRepository() throws IOException
    {
        OrderLocationRequest request = mock(OrderLocationRequest.class);
        when(request.getOrigin()).thenReturn(Lists.newArrayList(originLatitude, originLongitude));
        when(request.getDestination()).thenReturn(Lists.newArrayList(destinationLatitude, destinationLongitude));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(STATUS_OK);

        when(element.getStatus()).thenReturn(STATUS_OK);
        when(element.getDistance()).thenReturn(distance);
        when(distance.getValue()).thenReturn(new Integer(12345));
        when(row.getElements()).thenReturn(Lists.newArrayList(element));

        when(response.getRows()).thenReturn(Lists.newArrayList(row));
        when(response.getStatus()).thenReturn(STATUS_OK);

        when(googleDistanceApiService.connectToApi(Mockito.anyString(), Mockito.anyString())).thenReturn(response);
        when(orderRepository.save(any(OrderDetail.class))).thenReturn(orderDetail);

        OrderDetail result = orderService.postOrder(request);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(STATUS_UNASSIGNED, result.getStatus());
        assertEquals(new Integer(12345), result.getDistance());
    }

    @Test(expected = DataNotFoundException.class)
    public void shouldThrowDataNotFoundExceptionIfNoDistanceFound() throws IOException
    {
        OrderLocationRequest request = mock(OrderLocationRequest.class);
        when(request.getOrigin()).thenReturn(Lists.newArrayList(originLatitude, originLongitude));
        when(request.getDestination()).thenReturn(Lists.newArrayList(destinationLatitude, destinationLongitude));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(STATUS_OK);

        when(element.getStatus()).thenReturn("NOT_FOUND");
        when(row.getElements()).thenReturn(Lists.newArrayList(element));

        when(response.getRows()).thenReturn(Lists.newArrayList(row));
        when(response.getStatus()).thenReturn(STATUS_OK);

        when(googleDistanceApiService.connectToApi(Mockito.anyString(), Mockito.anyString())).thenReturn(response);

        assertNull(orderService.postOrder(request));
    }

    @Test(expected = DatabaseException.class)
    public void shouldThrowDatabaseException() throws IOException
    {
        OrderLocationRequest request = mock(OrderLocationRequest.class);
        when(request.getOrigin()).thenReturn(Lists.newArrayList(originLatitude, originLongitude));
        when(request.getDestination()).thenReturn(Lists.newArrayList(destinationLatitude, destinationLongitude));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(STATUS_OK);

        when(element.getStatus()).thenReturn(STATUS_OK);
        when(element.getDistance()).thenReturn(distance);
        when(distance.getValue()).thenReturn(new Integer(12345));
        when(row.getElements()).thenReturn(Lists.newArrayList(element));

        when(response.getRows()).thenReturn(Lists.newArrayList(row));
        when(response.getStatus()).thenReturn(STATUS_OK);

        when(googleDistanceApiService.connectToApi(Mockito.anyString(), Mockito.anyString())).thenReturn(response);
        when(orderRepository.save(any(OrderDetail.class))).thenThrow(DatabaseException.class);

        assertNull(orderService.postOrder(request));
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

    @Test(expected = DataNotFoundException.class)
    public void shouldValidateIfNoRecordFound()
    {
        OrderStatusRequest request = mock(OrderStatusRequest.class);
        when(request.getStatus()).thenReturn(STATUS_TAKEN);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(STATUS_UNASSIGNED);

        when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(orderDetail));
        when(orderRepository.updateOrderStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(0);

        OrderDetail result = orderService.updateOrder(request, "id");

        assertNotNull(result);
        assertEquals(STATUS_SUCCESS, result.getStatus());
    }

    @Test(expected = DatabaseException.class)
    public void shouldValidateIfStatusIsTaken()
    {
        OrderStatusRequest request = mock(OrderStatusRequest.class);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(STATUS_TAKEN);

        when(orderRepository.findById(Mockito.anyString())).thenReturn(Optional.of(orderDetail));

        assertNull(orderService.updateOrder(request, "id"));
    }

    @Test
    public void shouldSuccessfullyRetrieveOrdersByPage()
    {
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        @SuppressWarnings("unchecked")
        Page<OrderDetail> orderDetailPage = mock(Page.class);
        when(orderDetailPage.getContent()).thenReturn(Lists.newArrayList(orderDetail));
        when(orderRepository.findAll(pageableCaptor.capture())).thenReturn(orderDetailPage);

        List<OrderDetail> orderDetail = orderService.getPaginatedOrders(2, 5);

        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        verify(orderRepository).findAll(pageableCaptor.capture());

        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertFalse(orderDetail.isEmpty());
    }

    @Test
    public void shouldNotRetrieveOrderDetailsIfResultIsEmpty()
    {
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        @SuppressWarnings("unchecked")
        Page<OrderDetail> orderDetailPage = mock(Page.class);
        when(orderDetailPage.getContent()).thenReturn(Lists.newArrayList());
        when(orderRepository.findAll(pageableCaptor.capture())).thenReturn(orderDetailPage);

        List<OrderDetail> orderDetail = orderService.getPaginatedOrders(2, 5);

        verify(orderRepository).findAll(pageableCaptor.capture());

        assertTrue(orderDetail.isEmpty());

    }
}
