package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.OrderDTO;
import focodo_ecommerce.backend.dto.OrderStatusDTO;
import focodo_ecommerce.backend.dto.PaymentDTO;
import focodo_ecommerce.backend.dto.PaymentMethodDTO;
import focodo_ecommerce.backend.model.*;
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
            @RequestBody OrderRequestBody order,
            @RequestParam(name = "platform", required = false) String platform
            ) {
        return ApiResponse.<PaymentDTO>builder().result(orderService.createOrder(request, order.getCustomer(), order.getOrder(), platform)).build();
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

    @PutMapping("/updatePaymentStatus/{id}")
    public ApiResponse<OrderDTO> updatePaymentStatus(
            @PathVariable("id") String id,
            @RequestParam(name = "status") String status
    ) {
        return ApiResponse.<OrderDTO>builder().result(orderService.updatePaymentStatus(id, status)).build();
    }

    @PutMapping("/updateReviewOfOrder/{id}")
    public void updateReview(
            @PathVariable("id") String id
    ) {
        orderService.updateReviewOfOrder(id);
    }

    @GetMapping("/getOrdersOfUser")
    public ApiResponse<PaginationObjectResponse> getOrdersOfUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(orderService.getOrdersOfUser(page, size)).build();
    }

    @GetMapping("/getOrdersOfUserById/{id_user}")
    public ApiResponse<PaginationObjectResponse> getOrdersOfUserById(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @PathVariable("id_user") int id_user
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(orderService.getOrdersOfUserById(page, size, id_user)).build();
    }

    @GetMapping("/getAllOrderStatus")
    public ApiResponse<List<OrderStatusDTO>> getAllOrderStatus(
    ) {
        return ApiResponse.<List<OrderStatusDTO>>builder().result(orderService.getAllOrderStatus()).build();
    }

    @GetMapping("/getAllPaymentMethod")
    public ApiResponse<List<PaymentMethodDTO>> getAllPaymentMethod(
    ) {
        return ApiResponse.<List<PaymentMethodDTO>>builder().result(orderService.getAllPaymentMethod()).build();
    }
    @GetMapping("/getOrdersOfUserByOrderStatus")
    public ApiResponse<PaginationObjectResponse> getOrdersOfUserByOrderStatus(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "status") String status
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(orderService.getOrdersOfUserByOrderStatus(page, size, status)).build();
    }
}
