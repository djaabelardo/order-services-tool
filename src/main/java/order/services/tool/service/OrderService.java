package order.services.tool.service;

import static order.services.tool.utils.Constants.COMMA;
import static order.services.tool.utils.Constants.STATUS_OK;
import static order.services.tool.utils.Constants.STATUS_UNASSIGNED;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import order.services.tool.api.Distance;
import order.services.tool.api.Element;
import order.services.tool.api.GoogleDistanceApiService;
import order.services.tool.api.GoogleDistanceResponse;
import order.services.tool.api.Row;
import order.services.tool.exception.DataNotFoundException;
import order.services.tool.exception.DatabaseException;
import order.services.tool.model.OrderDetail;
import order.services.tool.model.OrderLocationRequest;
import order.services.tool.repository.OrderRepository;

@Service
public class OrderService
{

    @Autowired
    private GoogleDistanceApiService googleDistanceApiService;
    
    @Autowired
    private OrderRepository orderRepository;

    public OrderDetail postOrder(OrderLocationRequest request) throws IOException
    {

        final OrderDetail orderDetail = new OrderDetail();
        final String origin = combineList(request.getOrigin());
        final String destination = combineList(request.getDestination());

        GoogleDistanceResponse response = googleDistanceApiService.connectToApi(origin, destination);

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
}
