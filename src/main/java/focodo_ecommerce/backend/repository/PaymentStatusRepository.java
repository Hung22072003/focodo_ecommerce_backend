package focodo_ecommerce.backend.repository;
import focodo_ecommerce.backend.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
    PaymentStatus findByStatus(String status);
}
