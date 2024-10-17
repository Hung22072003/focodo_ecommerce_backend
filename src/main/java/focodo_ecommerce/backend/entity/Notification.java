package focodo_ecommerce.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "is_read")
    private boolean is_read = false;

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @Column(name = "type")
    private String type;

    @OneToOne
    @JoinColumn(name = "id_order")
    private Order order;
}
