package order.services.tool.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import net.minidev.json.JSONObject;
import order.services.tool.model.OrderLocationRequest;
import order.services.tool.model.OrderStatusRequest;
import order.services.tool.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest
{
    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    private static final String originLatitude = "40.6905615";
    private static final String originLongitude = "-73.9976592";
    private static final String destinationLatitude = "40.6905615";
    private static final String destinationLongitude = "-73.9976592";

    private static final String originLatitudeAlpha = "40.a6905615";

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .build();

    }

    @Test
    public void postOrderShouldSaveAndReturnDetails() throws Exception
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("origin", Lists.newArrayList(originLongitude, originLatitude));
        jsonObj.put("destination", Lists.newArrayList(destinationLongitude, destinationLatitude));

        mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(jsonObj)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        verify(orderService, times(1)).postOrder(any(OrderLocationRequest.class));
    }

    @Test
    public void postOrderShouldReturnBadRequestIfNotTwoElements() throws Exception
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("origin", Lists.newArrayList(originLongitude));
        jsonObj.put("destination", Lists.newArrayList(destinationLongitude, destinationLatitude));

        mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(jsonObj)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        verify(orderService, never()).postOrder(any(OrderLocationRequest.class));
    }

    @Test
    public void postOrderShouldReturnBadRequestIfIncorrectCoordinates() throws Exception
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("origin", Lists.newArrayList(originLatitudeAlpha, originLongitude));
        jsonObj.put("destination", Lists.newArrayList(destinationLatitude, destinationLongitude));

        mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(jsonObj)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        verify(orderService, never()).postOrder(any(OrderLocationRequest.class));
    }

    @Test
    public void postOrderShouldReturnBadRequestIfMissingRequest() throws Exception
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("destination", Lists.newArrayList(destinationLatitude, destinationLongitude));

        mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(jsonObj)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        verify(orderService, never()).postOrder(any(OrderLocationRequest.class));
    }

    @Test
    public void takeOrderShouldUpdateOrderStatus() throws Exception
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("status", "TAKEN");

        mockMvc.perform(patch("/orders/anyId")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(jsonObj)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        verify(orderService, times(1)).updateOrder(any(OrderStatusRequest.class), Mockito.anyString());
    }

    @Test
    public void takeOrderShouldReturnBadRequestIfNotTaken() throws Exception
    {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("status", "RETRIEVED");

        mockMvc.perform(patch("/orders/anyId")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(jsonObj)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        verify(orderService, never()).updateOrder(any(OrderStatusRequest.class), Mockito.anyString());
    }

    @Test
    public void takeOrderShouldReturnBadRequestIfNoStatus() throws Exception
    {
        JSONObject jsonObj = new JSONObject();

        mockMvc.perform(patch("/orders/anyId")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(jsonObj)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        verify(orderService, never()).updateOrder(any(OrderStatusRequest.class), Mockito.anyString());
    }

    @Test
    public void getOrderShouldRetrieveOrderDetails() throws Exception
    {

        mockMvc.perform(get("/orders")
                .param("page", "1")
                .param("limit", "2")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        verify(orderService, times(1)).getPaginatedOrders(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void getOrderShouldReturnBadRequestIfPageIsAlpha() throws Exception
    {

        mockMvc.perform(get("/orders")
                .param("page", "a")
                .param("limit", "2")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        verify(orderService, never()).getPaginatedOrders(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void getOrderShouldReturnBadRequestIfPageIsLessThanOne() throws Exception
    {

        mockMvc.perform(get("/orders")
                .requestAttr("page", 0)
                .requestAttr("limit", 1)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        verify(orderService, never()).getPaginatedOrders(Mockito.anyInt(), Mockito.anyInt());
    }

    static byte[] convertObjectToJsonBytes(Object object) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
