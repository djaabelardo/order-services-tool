package order.services.tool.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import order.services.tool.model.OrderDetail;
import order.services.tool.model.OrderLocationRequest;
import order.services.tool.model.OrderStatusRequest;
import order.services.tool.service.OrderService;

@RestController
@Validated
public class OrderController
{
    
    @Autowired
    private OrderService orderService;

    @PostMapping(path = "/orders", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public OrderDetail postOrder(@Valid @RequestBody OrderLocationRequest request) throws IOException
    {
        return orderService.postOrder(request);

    }

    @PatchMapping(path = "/orders/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public OrderDetail takeOrder(@PathVariable("id") String id, @Valid @RequestBody OrderStatusRequest request)
    {
        return orderService.updateOrder(request, id);
    }
    
    @GetMapping(path = "/orders", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDetail> getOrder(@NotNull @Min(1) @RequestParam Integer page,
            @NotNull @Min(1) @RequestParam Integer limit)
    {
        return orderService.getPaginatedOrders(page, limit);
    }

}
