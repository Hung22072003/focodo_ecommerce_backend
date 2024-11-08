package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.*;
import focodo_ecommerce.backend.entity.Order;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
    public Long rateCustomerReturning() {
        return orderRepository.countUsersWithMultipleOrders();
    }

    @Override
    public List<ChartDTO> rateCustomerByProvince() {
        List<User> users = userRepository.findAllCustomer();
        List<ChartDTO> result = new ArrayList<>();
        users.forEach((user) ->{
            AtomicBoolean check = new AtomicBoolean(false);
            if(!result.isEmpty()) {
                result.forEach((item) -> {
                    if(item.getName().equals(user.getProvince()) || (item.getName().equals("Không xác định") && user.getProvince() == null)) {
                        check.set(true);
                        item.setValue(item.getValue() + 1);
                    }
                });
            }

            if(!check.get()) {
                result.add(new ChartDTO(user.getProvince() != null ? user.getProvince() : "Không xác định", 1));
            }
        });
        return result;
    }

    @Override
    public List<ChartDTO> ratePaymentMethod() {
        List<Order> orders = orderRepository.findAll();
        List<ChartDTO> result = new ArrayList<>();
        orders.forEach((order) -> {
            AtomicBoolean check = new AtomicBoolean(false);
            if(!result.isEmpty()) {
                result.forEach((item) -> {
                    if(item.getName().equals(order.getPaymentMethod().getMethod())) {
                        check.set(true);
                        item.setValue(item.getValue() + 1);
                    }
                });
            }

            if(!check.get()) {
                result.add(new ChartDTO(order.getPaymentMethod().getMethod(), 1));
            }
        });
        return result;
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

    @Override
    public List<ProductDTO> topProductRating() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> result = products.stream().map(ProductDTO::new).filter((productDTO) -> productDTO.getReview() != 0.0).sorted(Comparator.comparing(ProductDTO::getReview).reversed()).collect(Collectors.toList());
        return result.size() > 8 ? result.subList(0,8) : result;
    }

    @Override
    public Integer totalProduct() {
        return productRepository.findAll().size();
    }

    @Override
    public Integer quantityProductInActive() {
        return productRepository.findAll().size() - productRepository.findAllProductActive().size();
    }

    @Override
    public Integer quantityProductActive() {
        return productRepository.findAllProductActive().size();
    }
}
