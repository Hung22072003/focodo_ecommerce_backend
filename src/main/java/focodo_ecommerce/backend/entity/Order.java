package focodo_ecommerce.backend.entity;

import focodo_ecommerce.backend.model.OrderRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "\"order\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id_order;
    private String description;
    private LocalDateTime order_date;
    private Long shipping_price;
    private Long discount_price;
    private Long total_price;
    private Long final_price;
    private boolean is_check = false;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "payment_status_id")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Notification notification;

    public Order(OrderRequest orderRequest) {
        this.description = orderRequest.getDescription();
        this.order_date = LocalDateTime.now();
        this.shipping_price = orderRequest.getShipping_price();
        this.discount_price = orderRequest.getDiscount_price();
        this.total_price = orderRequest.getTotal_price();
        this.final_price = orderRequest.getFinal_price();
    }
}
