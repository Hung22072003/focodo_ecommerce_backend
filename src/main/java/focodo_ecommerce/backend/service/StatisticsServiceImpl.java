package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.dto.RevenuePerDayDTO;
import focodo_ecommerce.backend.dto.RevenuePerMonthDTO;
import focodo_ecommerce.backend.dto.UserDTO;
import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.User;
import focodo_ecommerce.backend.repository.OrderRepository;
import focodo_ecommerce.backend.repository.ProductRepository;
import focodo_ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Override
    public List<RevenuePerMonthDTO> getRevenueOneYear(int year) {
        List<RevenuePerMonthDTO> revenue = new ArrayList<>();
        for(int i = 1; i<=12; i++) {
            revenue.add(new RevenuePerMonthDTO(i, orderRepository.sumFinalPriceByDate("%Y-%m", year+ "-" + (i < 10 ? "0" + i : i))));
        }
        return revenue;
    }

    @Override
    public List<RevenuePerDayDTO> getRevenueLastSevenDays() {
        LocalDate currentDay = LocalDate.now();
        List<RevenuePerDayDTO> revenue = new ArrayList<>();
        currentDay.minusDays(7).datesUntil(currentDay).forEach((localDate) -> {
            revenue.add(new RevenuePerDayDTO(localDate, orderRepository.sumFinalPriceByDate("%Y-%m-%d", localDate.format(DateTimeFormatter.ISO_DATE))));
        });
        return revenue;
    }

    @Override
    public BigDecimal getRevenueToday() {
        LocalDate currentDay = LocalDate.now();
        return orderRepository.sumFinalPriceByDate("%Y-%m-%d", currentDay.format(DateTimeFormatter.ISO_DATE));
    }

    @Override
    public BigDecimal totalRevenue() {
        return orderRepository.totalRevenue();
    }

    @Override
    public Integer totalCustomer() {
        return userRepository.findAllCustomer().size();
    }

    @Override
    public Integer totalOrder() {
        return orderRepository.findAll().size();
    }

    @Override
    public List<UserDTO> topCustomerBySpending() {
        Page<User> users = userRepository.topCustomerBySpending(PageRequest.of(0,8, Sort.by("total_money").descending()));
        return users.get().map(UserDTO::new).toList();
    }

    @Override
    public List<ProductDTO> topProductBestSeller() {
        Page<Product> products = productRepository.findProductsBestSeller(PageRequest.of(0, 8, Sort.by("sold_quantity").descending()));
        return products.get().map(ProductDTO::new).toList();
    }

}
