package focodo_ecommerce.backend.entity;

import focodo_ecommerce.backend.model.CustomerRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String full_name;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    public Customer(CustomerRequest customerRequest) {
        this.full_name = customerRequest.getFull_name();
        this.phone = customerRequest.getPhone();
        this.address = customerRequest.getAddress();
        this.province = customerRequest.getProvince();
        this.district = customerRequest.getDistrict();
        this.ward = customerRequest.getWard();
    }
}
