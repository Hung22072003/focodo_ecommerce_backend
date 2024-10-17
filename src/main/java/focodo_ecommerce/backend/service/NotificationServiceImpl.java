package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.NotificationDTO;
import focodo_ecommerce.backend.entity.Notification;
import focodo_ecommerce.backend.entity.Order;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.OrderRequest;
import focodo_ecommerce.backend.model.Pagination;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.repository.NotificationRepository;
import focodo_ecommerce.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;
    @Override
    @Transactional
    public NotificationDTO createNotification(String id_order) {
        Order foundOrder = orderRepository.findById(id_order).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        Notification notification = new Notification();
        notification.setOrder(foundOrder);
        notification.setCreateTime(LocalDateTime.now());
        if(foundOrder.getOrderStatus().getStatus().equals("Chưa xác nhận")) {
            notification.setType("create-order");
            notification.setContent("Đơn hàng <strong>" + id_order + "</strong> cần xác nhận");
        } else if(foundOrder.getOrderStatus().getStatus().equals("Đã hủy")) {
            Notification foundNotification = foundOrder.getNotification();
            if(foundNotification != null) {
                foundNotification.set_read(false);
                foundNotification.setCreateTime(LocalDateTime.now());
                foundNotification.setType("cancel-order");
                foundNotification.setContent("Đơn hàng <strong>" + id_order + "</strong> đã bị hủy");
                return new NotificationDTO(foundNotification);
            } else {
                notification.setType("cancel-order");
                notification.setContent("Đơn hàng <strong>" + id_order + "</strong> đã bị hủy");
            }
        }
        return new NotificationDTO(notificationRepository.save(notification));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public PaginationObjectResponse getAllNotifications(int page, int size) {
        Page<Notification> notifications = notificationRepository.findAll(PageRequest.of(page, size, Sort.by("createTime").descending()));
        return PaginationObjectResponse.builder().data(notifications.stream().map(NotificationDTO::new).toList()).pagination(new Pagination(notifications.getTotalElements(), notifications.getTotalPages(), notifications.getNumber())).build();
    }

    @Override
    @Transactional
    public void updateNotification(int id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.set_read(true);
    }
}
