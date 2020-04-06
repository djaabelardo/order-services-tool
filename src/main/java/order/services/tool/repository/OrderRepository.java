package order.services.tool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import order.services.tool.model.OrderDetail;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetail, String>
{
    @Modifying
    @Query("update OrderDetail o set o.status = :status where o.id = :id")
    public int updateOrderStatus(@Param("status") String status, @Param("id") String id);
}
