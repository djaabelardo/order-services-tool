package order.services.tool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import order.services.tool.model.OrderDetail;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetail, String>
{

}
