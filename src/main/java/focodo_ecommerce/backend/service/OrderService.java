package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.OrderDTO;
import focodo_ecommerce.backend.dto.OrderStatusDTO;
import focodo_ecommerce.backend.dto.PaymentDTO;
import focodo_ecommerce.backend.dto.PaymentMethodDTO;
import focodo_ecommerce.backend.model.CustomerRequest;
import focodo_ecommerce.backend.model.OrderRequest;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {
    PaymentDTO createOrder(HttpServletRequest request, CustomerRequest customerRequest, OrderRequest orderRequest, String platform);

    void setPaymentStatus(String id_order, int status);

    void setOrderStatus(String idOrder, int status);

    OrderDTO getOrderById(String id);

    PaginationObjectResponse getAllOrders(int page, int size);

    OrderDTO updateOrderStatus(String id, String status);

    PaginationObjectResponse getOrdersOfUser(int page, int size);

    PaginationObjectResponse getOrdersOfUserByOrderStatus(int page, int size, String status);

    void updateReviewOfOrder(String id);

    List<OrderStatusDTO> getAllOrderStatus();

    List<PaymentMethodDTO> getAllPaymentMethod();
}
