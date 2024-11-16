package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    @Query("select u from User u where u.role = 'USER' or u.role = 'CUSTOMER'")
    List<User> findAllCustomer();

    @Query("select u from User u where u.role = 'USER' or u.role = 'CUSTOMER'")
    Page<User> findAllCustomer(Pageable pageable);
    @Query("select u from User u where (u.role = 'USER' or u.role = 'CUSTOMER') and u.total_money > 0")
    Page<User> topCustomerBySpending(Pageable pageable);
}
