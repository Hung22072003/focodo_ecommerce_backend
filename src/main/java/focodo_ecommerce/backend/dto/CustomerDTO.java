package focodo_ecommerce.backend.dto;

import focodo_ecommerce.backend.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String full_name;
    private String phone;
    private String address;
    private String province;
    private String district;
    private String ward;

    public CustomerDTO(Customer customer) {
        this.full_name = customer.getFull_name();
        this.phone = customer.getPhone();
        this.address = customer.getAddress();
        this.province = customer.getProvince();
        this.district = customer.getDistrict();
        this.ward = customer.getWard();
    }
}
