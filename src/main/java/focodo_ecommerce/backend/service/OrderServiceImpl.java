package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.OrderDTO;
import focodo_ecommerce.backend.dto.OrderStatusDTO;
import focodo_ecommerce.backend.dto.PaymentDTO;
import focodo_ecommerce.backend.dto.PaymentMethodDTO;
import focodo_ecommerce.backend.entity.*;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.CustomerRequest;
import focodo_ecommerce.backend.model.OrderRequest;
import focodo_ecommerce.backend.model.Pagination;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.repository.*;
import focodo_ecommerce.backend.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final ProductRepository productRepository;
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    @Override
    @Transactional
    public PaymentDTO createOrder(HttpServletRequest request, CustomerRequest customerRequest, OrderRequest orderRequest, String platform) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(orderRequest.getId_voucher() != null) {
            voucherRepository.findById(orderRequest.getId_voucher()).ifPresent(voucher -> voucher.setQuantity(voucher.getQuantity() - 1));
        }

        String id_order = VNPayUtil.getRandomNumber(8);
        while(orderRepository.existsById(id_order)) {
            id_order = VNPayUtil.getRandomNumber(8);
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderRequest.getPayment_method()).orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));
        Order newOrder = new Order(orderRequest);
        newOrder.setId_order(id_order);
        if(!authentication.getName().equals("anonymousUser")) {
            User foundUser = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            foundUser.setFull_name(customerRequest.getFull_name());
            foundUser.setPhone(customerRequest.getPhone());
            foundUser.setDistrict(customerRequest.getDistrict());
            foundUser.setAddress(customerRequest.getAddress());
            foundUser.setProvince(customerRequest.getProvince());
            foundUser.setWard(customerRequest.getWard());

            List<Cart> carts = foundUser.getCarts().stream().filter(Cart::getCheck).toList();
            cartRepository.deleteAllInBatch(carts);
            newOrder.setUser(foundUser);
        } else {
            User user = userRepository.findByPhone(customerRequest.getPhone()).orElse(null);
            if(user == null) {
                user = userRepository.save(new User(customerRequest));
            } else {
                user.setFull_name(customerRequest.getFull_name());
                user.setDistrict(customerRequest.getDistrict());
                user.setAddress(customerRequest.getAddress());
                user.setProvince(customerRequest.getProvince());
                user.setWard(customerRequest.getWard());
                user = userRepository.save(user);
            }
            orderRequest.getDetails().forEach((order) -> {
                Product product = productRepository.findById(order.getId_product()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                product.setQuantity(product.getQuantity() - product.getPackage_quantity() * order.getQuantity());
            });
            newOrder.setUser(user);
        }
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setOrderStatus(orderStatusRepository.findByStatus("Chưa xác nhận").orElseThrow(() -> new AppException(ErrorCode.ORDER_STATUS_NOT_FOUND)));
        newOrder.setPaymentStatus(paymentStatusRepository.findByStatus("Chưa thanh toán"));
        orderRepository.save(newOrder);
        List<OrderDetail> orderDetails = orderRequest.getDetails().stream().map((orderDetailRequest -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(productRepository.findById(orderDetailRequest.getId_product()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setUnit_price(orderDetailRequest.getUnit_price());
            orderDetail.setTotal_price(orderDetailRequest.getUnit_price() * orderDetailRequest.getQuantity());
            return orderDetail;
        })).toList();
        orderDetailRepository.saveAll(orderDetails);

        notificationService.createNotification(id_order);
        if(paymentMethod.getMethod().equals("VNPAY")) {
            return paymentService.createVnPayPayment(request, newOrder.getFinal_price(), newOrder.getId_order(), platform);
        }

        return new PaymentDTO("ok", "success", id_order, "");
    }

    @Override
    @Transactional
    public void setPaymentStatus(String id_order, int status) {
        Order order = orderRepository.findById(id_order).orElseThrow();
        order.setPaymentStatus(paymentStatusRepository.findById(status).orElse(null));
    }

    @Override
    @Transactional
    public void setOrderStatus(String idOrder, int status) {
        Order order = orderRepository.findById(idOrder).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setOrderStatus(orderStatusRepository.findById(status).orElse(null));
        if(status == 4) {
            notificationService.createNotification(idOrder);
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                Product product = productRepository.findById(orderDetail.getProduct().getId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                product.setQuantity(product.getQuantity() + product.getPackage_quantity() * orderDetail.getQuantity());
            }
        }
    }

    @Override
    public OrderDTO getOrderById(String id) {
        return new OrderDTO(orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public PaginationObjectResponse getAllOrders(int page, int size) {
        Page<Order> orders = orderRepository.findAll(PageRequest.of(page, size, Sort.by("order_date").descending()));
        return PaginationObjectResponse.builder().data(orders.get().map(OrderDTO::new).toList()).pagination(new Pagination(orders.getTotalElements(),orders.getTotalPages(),orders.getNumber())).build();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public OrderDTO updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        OrderStatus orderStatus = orderStatusRepository.findByStatus(status).orElseThrow(() -> new AppException(ErrorCode.ORDER_STATUS_NOT_FOUND));
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean check = false;
        if(user.getRole().toString().equals("ADMIN")) {
            order.setOrderStatus(orderStatus);
            check = true;
        } else if(order.getUser() != null){
            if(user.getId() == order.getUser().getId() && order.getOrderStatus().getStatus().equals("Chưa xác nhận") && orderStatus.getStatus().equals("Đã hủy")) {
                check = true;
                order.setOrderStatus(orderStatus);
            }
        }
        if(check) {
            if(status.equals("Đã giao")) {
                setPaymentStatus(order.getId_order(), 1);
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    Product product = productRepository.findById(orderDetail.getProduct().getId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                    product.setSold_quantity(product.getSold_quantity() + product.getPackage_quantity() * orderDetail.getQuantity());
                }
            } else if(status.equals("Đã hủy")) {
                notificationService.createNotification(id);
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    Product product = productRepository.findById(orderDetail.getProduct().getId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                    product.setQuantity(product.getQuantity() + product.getPackage_quantity() * orderDetail.getQuantity());
                }
            }
        }
        return new OrderDTO(order);
    }

    @Override
    public PaginationObjectResponse getOrdersOfUser(int page, int size) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Page<Order> orders = orderRepository.findAllByUser(user, PageRequest.of(page, size, Sort.by("order_date").descending()));
        return PaginationObjectResponse.builder().data(orders.get().map(OrderDTO::new).toList()).pagination(new Pagination(orders.getTotalElements(),orders.getTotalPages(),orders.getNumber())).build();
    }

    @Override
    public PaginationObjectResponse getOrdersOfUserByOrderStatus(int page, int size, String status) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Page<Order> orders = orderRepository.findAllByUserAndOrderStatus(user, status.trim(), PageRequest.of(page, size, Sort.by("order_date").descending()));
        return PaginationObjectResponse.builder().data(orders.get().map(OrderDTO::new).toList()).pagination(new Pagination(orders.getTotalElements(),orders.getTotalPages(),orders.getNumber())).build();
    }

    @Override
    @Transactional
    public void updateReviewOfOrder(String id) {
        Order foundOrder = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if(foundOrder.getOrderStatus().getStatus().equals("Đã giao") && !foundOrder.is_check()) foundOrder.set_check(true);
    }

    @Override
    public List<OrderStatusDTO> getAllOrderStatus() {
        return orderStatusRepository.findAll().stream().map(OrderStatusDTO::new).toList();
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethod() {
        return paymentMethodRepository.findAll().stream().map(PaymentMethodDTO::new).toList();
    }
}
