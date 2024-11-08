package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.Order;
import focodo_ecommerce.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> , JpaSpecificationExecutor<Order> {
    @Query("select o from Order o ")
    Page<Order> findAll(Pageable pageable);
    @Query("select o from Order o where o.user = :user")
    Page<Order> findAllByUser(User user, PageRequest orderDate);
    @Query("select o from Order o where o.user = :user and o.orderStatus.status = :status")
    Page<Order> findAllByUserAndOrderStatus(User user, String status, PageRequest orderDate);

    @Query(value = "SELECT SUM(o.final_price - o.shipping_price) FROM `order` o " +
            "WHERE o.order_status_id = 3 AND DATE_FORMAT(o.order_date, :formatDate) = :date", nativeQuery = true)
    BigDecimal sumFinalPriceByDate(@Param("formatDate") String formatDate, @Param("date") String date);

    @Query(value = "SELECT SUM(o.final_price - o.shipping_price) FROM `order` o " +
            "WHERE o.payment_status_id = 1", nativeQuery = true)
    BigDecimal totalRevenue();

    @Query("SELECT COUNT(u.id) " +
            "FROM User u " +
            "WHERE u.id IN (" +
            "    SELECT u.id " +
            "    FROM Order o JOIN User u ON o.user.id = u.id " +
            "    WHERE o.orderStatus.id = 3 " +
            "    GROUP BY u.id " +
            "    HAVING COUNT(o.id) > 1" +
            ")")
    Long countUsersWithMultipleOrders();
}
