package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.dto.RevenuePerDayDTO;
import focodo_ecommerce.backend.dto.RevenuePerMonthDTO;
import focodo_ecommerce.backend.dto.UserDTO;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {
    List<RevenuePerMonthDTO> getRevenueOneYear(int year);

    BigDecimal totalRevenue();

    Integer totalCustomer();

    Integer totalOrder();

    List<UserDTO> topCustomerBySpending();

    List<ProductDTO> topProductBestSeller();

    List<RevenuePerDayDTO> getRevenueLastSevenDays();

    BigDecimal getRevenueToday();
}
