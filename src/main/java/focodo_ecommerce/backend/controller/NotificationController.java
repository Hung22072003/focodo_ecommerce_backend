package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.NotificationDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.PaginationObjectResponse;
import focodo_ecommerce.backend.service.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @PostMapping("create")
    public ApiResponse<NotificationDTO> createNotification(
            @RequestParam(name = "id_order") String id_order
    ) {
        return ApiResponse.<NotificationDTO>builder().result(notificationService.createNotification(id_order)).build();
    }

    @GetMapping("")
    public ApiResponse<PaginationObjectResponse> getAllNotifications(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue =  "10") int size
    ) {
        return ApiResponse.<PaginationObjectResponse>builder().result(notificationService.getAllNotifications(page, size)).build();
    }

    @PutMapping("update/{id}")
    public void updateNotification(
            @PathVariable("id") int id
    ) {
        notificationService.updateNotification(id);
    }
}
