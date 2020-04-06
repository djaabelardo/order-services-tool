package order.services.tool.service;

import static order.services.tool.utils.Constants.COMMA;
import static order.services.tool.utils.Constants.STATUS_OK;
import static order.services.tool.utils.Constants.STATUS_SUCCESS;
import static order.services.tool.utils.Constants.STATUS_TAKEN;
import static order.services.tool.utils.Constants.STATUS_UNASSIGNED;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

@Service
public class OrderService
{

    private GoogleDistanceApiService googleDistanceApiService;

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, GoogleDistanceApiService googleDistanceApiService)
    {
        this.orderRepository = orderRepository;
        this.googleDistanceApiService = googleDistanceApiService;
    }

    public OrderDetail postOrder(OrderLocationRequest request) throws IOException
    {

        final OrderDetail orderDetail = new OrderDetail();
        final String origin = combineList(request.getOrigin());
        final String destination = combineList(request.getDestination());

        final GoogleDistanceResponse response = googleDistanceApiService.connectToApi(origin, destination);

        if (response.getStatus().equals(STATUS_OK))
        {
            calculateTotalDistance(orderDetail, response);
            orderDetail.setStatus(STATUS_UNASSIGNED);
            orderDetail.setId(UUID.randomUUID().toString());

            try
            {
                orderRepository.save(orderDetail);
            }
            catch (Exception e)
            {
                throw new DatabaseException(e.getMessage());
            }

        }
        return orderDetail;
    }

    private void calculateTotalDistance(final OrderDetail orderDetail, GoogleDistanceResponse response)
    {
        List<Element> elements = response.getRows()
                .stream()
                .map(Row::getElements)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Integer> distances = elements.stream()
                .filter(elem -> elem.getStatus().equals(STATUS_OK))
                .map(Element::getDistance)
                .map(Distance::getValue)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(distances))
        {
            throw new DataNotFoundException("Distance not found.");
        }
        orderDetail.setDistance(distances.stream().mapToInt(Integer::intValue).sum());
    }

    private String combineList(List<String> originDestinations)
    {
        return String.join(COMMA, originDestinations);
    }

    @Transactional
    public OrderDetail updateOrder(OrderStatusRequest request, String id)
    {
        final OrderDetail orderDetail = new OrderDetail();
        orderRepository.findById(id).ifPresent(rec -> {
            if (rec.getStatus().equals(STATUS_TAKEN))
            {
                throw new DatabaseException("Order already taken for id: " + id);
            }
        });

        if (orderRepository.updateOrderStatus(request.getStatus(), id) == 0)
        {
            throw new DataNotFoundException("No record found for id: " + id);
        }
        orderDetail.setStatus(STATUS_SUCCESS);
        return orderDetail;
    }

    public List<OrderDetail> getPaginatedOrders(Integer page, Integer limit)
    {

        Pageable paging = PageRequest.of(page - 1, limit, Sort.by("distance"));
        Page<OrderDetail> pagedResult = orderRepository.findAll(paging);

        return pagedResult.getContent();
    }
}
