package order.services.tool.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import order.services.tool.OrderApplication;
import order.services.tool.model.OrderDetail;
import order.services.tool.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { OrderApplication.class })
@ActiveProfiles("test")
public class OrderRepositoryTest
{

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void shouldSuccessfullySaveData()
    {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId("id");
        orderDetail.setDistance(new Integer(12345));
        orderDetail.setStatus("SUCCESS");
        OrderDetail orderDetailRecord = orderRepository.save(orderDetail);

        assertNotNull(orderDetailRecord);
        assertEquals("id", orderDetailRecord.getId());
    }

    @Test
    public void shouldSuccessfullyRetrieveAllData()
    {
        Pageable paging = PageRequest.of(0, 2);
        Page<OrderDetail> orderDetailRecord = orderRepository.findAll(paging);
        assertNotNull(orderDetailRecord);
    }
 
}
