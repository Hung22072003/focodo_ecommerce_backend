package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.*;

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

    Long rateCustomerReturning();

    List<ChartDTO> rateCustomerByProvince();

    List<ChartDTO> ratePaymentMethod();

    List<ProductDTO> topProductRating();

    Integer quantityProductInActive();

    Integer quantityProductActive();

    Integer totalProduct();
}
