package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.Order;
import focodo_ecommerce.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> , JpaSpecificationExecutor<Order> {
    @Query("select o from Order o")
    Page<Order> findAll(Pageable pageable);
    @Query("select o from Order o where o.user = :user")
    Page<Order> findAllByUser(User user, PageRequest orderDate);
    @Query("select o from Order o where o.user = :user and o.orderStatus.status = :status")
    Page<Order> findAllByUserAndOrderStatus(User user, String status, PageRequest orderDate);
}
