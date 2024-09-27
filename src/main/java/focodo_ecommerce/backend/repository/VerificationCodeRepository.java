package focodo_ecommerce.backend.repository;

import focodo_ecommerce.backend.entity.User;
import focodo_ecommerce.backend.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {
    @Query("select vc from VerificationCode vc where vc.otp = ?1 and vc.user = ?2")
    Optional<VerificationCode> findByOtpAndUser(String otp, User user);

    boolean existsByUser(User user);

    Optional<VerificationCode> findByUser(User user);
}
