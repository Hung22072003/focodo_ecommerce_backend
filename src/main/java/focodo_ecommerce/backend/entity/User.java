package focodo_ecommerce.backend.entity;

import focodo_ecommerce.backend.model.CustomerRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String full_name;
    private String email;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String username;
    private String password;
    private String avatar;
    private Long total_money = 0L;
    private Integer total_order = 0;
    private LocalDateTime created_date;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private VerificationCode verificationCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    public User(CustomerRequest customerRequest) {
        this.full_name = customerRequest.getFull_name();
        this.phone = customerRequest.getPhone();
        this.address = customerRequest.getAddress();
        this.province = customerRequest.getProvince();
        this.district = customerRequest.getDistrict();
        this.ward = customerRequest.getWard();
        this.role = Role.CUSTOMER;
        this.total_money = 0L;
        this.total_order = 0;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
