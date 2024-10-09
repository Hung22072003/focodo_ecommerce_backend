package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.OrderDTO;
import focodo_ecommerce.backend.dto.PaymentDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.CustomerRequest;
import focodo_ecommerce.backend.model.OrderRequest;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${spring.application.api-prefix}/orders")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/create")
    public ApiResponse<PaymentDTO> createOrder(
            HttpServletRequest request,
            @RequestPart(name = "customer")CustomerRequest customerRequest,
            @RequestPart(name = "order")OrderRequest orderRequest
            ) {
        return ApiResponse.<PaymentDTO>builder().result(orderService.createOrder(request, customerRequest, orderRequest)).build();
    }

    @GetMapping("/getOrderById/{id}")
    public ApiResponse<OrderDTO> getOrderById(
            @PathVariable("id") String id
    ) {
        return ApiResponse.<OrderDTO>builder().result(orderService.getOrderById(id)).build();
    }

    @GetMapping("")
    public ApiResponse<PaginationObjectResponse> getAllOrders(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ApiResponse.<PaginationObjectResponse>builder().result(orderService.getAllOrders(page, size)).build();
    }

    @PutMapping("/updateOrderStatus/{id}")
    public ApiResponse<OrderDTO> updateOrderStatus(
            @PathVariable("id") String id,
            @RequestParam(name = "status") String status
    ) {
        return ApiResponse.<OrderDTO>builder().result(orderService.updateOrderStatus(id, status)).build();
    }

    @GetMapping("/getOrdersOfUser")
    public ApiResponse<PaginationObjectResponse> getOrdersOfUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(orderService.getOrdersOfUser(page, size)).build();
    }

    @GetMapping("/getOrdersOfUserByOrderStatus")
    public ApiResponse<PaginationObjectResponse> getPurchaseHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "status") String status
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(orderService.getOrdersOfUserByOrderStatus(page, size, status)).build();
    }
}
