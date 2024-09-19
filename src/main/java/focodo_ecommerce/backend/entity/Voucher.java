package focodo_ecommerce.backend.entity;

import focodo_ecommerce.backend.dto.VoucherDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    private String id_voucher;
    private LocalDate start_date;
    private LocalDate end_date;
    private int quantity;
    private Long min_total;
    private Long discount_price;
    private boolean expired;

    public Voucher(VoucherDTO voucherDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.id_voucher = voucherDTO.getId_voucher();
        this.start_date = LocalDate.parse(voucherDTO.getStart_date(), formatter);
        this.end_date = LocalDate.parse(voucherDTO.getEnd_date(), formatter);
        this.quantity = voucherDTO.getQuantity();
        this.min_total = voucherDTO.getMin_total();
        this.discount_price = voucherDTO.getDiscount_price();
        this.expired = voucherDTO.isExpired();
    }
}
