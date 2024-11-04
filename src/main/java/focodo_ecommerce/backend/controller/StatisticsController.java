package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.ProductDTO;
import focodo_ecommerce.backend.dto.RevenuePerDayDTO;
import focodo_ecommerce.backend.dto.RevenuePerMonthDTO;
import focodo_ecommerce.backend.dto.UserDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@CrossOrigin
public class StatisticsController {
    private final StatisticsService statisticsService;
    @GetMapping("/revenue/oneYear")
    public ApiResponse<List<RevenuePerMonthDTO>> getRevenueOneYear(@RequestParam(name = "year") int year) {
        return ApiResponse.<List<RevenuePerMonthDTO>>builder().result(statisticsService.getRevenueOneYear(year)).build();
    }

    @GetMapping("/revenue/lastSevenDays")
    public ApiResponse<List<RevenuePerDayDTO>> getRevenueLastSevenDays() {
        return ApiResponse.<List<RevenuePerDayDTO>>builder().result(statisticsService.getRevenueLastSevenDays()).build();
    }

    @GetMapping("/revenue/today")
    public ApiResponse<BigDecimal> getRevenueToday() {
        return ApiResponse.<BigDecimal>builder().result(statisticsService.getRevenueToday()).build();
    }
    @GetMapping("/revenue/total")
    public ApiResponse<BigDecimal> totalRevenue() {
        return ApiResponse.<BigDecimal>builder().result(statisticsService.totalRevenue()).build();
    }

    @GetMapping("/customer/total")
    public ApiResponse<Integer> totalCustomer() {
        return ApiResponse.<Integer>builder().result(statisticsService.totalCustomer()).build();
    }

    @GetMapping("/customer/topCustomerBySpending")
    public ApiResponse<List<UserDTO>> topCustomerBySpending() {
        return ApiResponse.<List<UserDTO>>builder().result(statisticsService.topCustomerBySpending()).build();
    }

    @GetMapping("/order/total")
    public ApiResponse<Integer> totalOrder() {
        return ApiResponse.<Integer>builder().result(statisticsService.totalOrder()).build();
    }

    @GetMapping("/product/topProductBestSeller")
    public ApiResponse<List<ProductDTO>> topProductBestSeller() {
        return ApiResponse.<List<ProductDTO>>builder().result(statisticsService.topProductBestSeller()).build();
    }
}
