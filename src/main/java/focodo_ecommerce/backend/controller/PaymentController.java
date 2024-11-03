package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.PaymentDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.service.OrderService;
import focodo_ecommerce.backend.service.PaymentService;
import focodo_ecommerce.backend.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("${spring.application.api-prefix}/payment")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    @GetMapping("/vn-pay")
    public ApiResponse<PaymentDTO> pay(HttpServletRequest request) throws IOException {
        return ApiResponse.<PaymentDTO>builder().result(paymentService.createVnPayPayment(request, 10000L, VNPayUtil.getRandomNumber(8), "web")).build();
    }
    @GetMapping("/vn-pay-callback")
    public ApiResponse<PaymentDTO> payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String status = request.getParameter("vnp_ResponseCode");
        String id_order = request.getParameter("vnp_TxnRef");
        String platform = request.getParameter("platform");
        System.out.println(platform);
        if (status.equals("00")) {
            orderService.setPaymentStatus(id_order, 1);
            if(platform.equals("web")) response.sendRedirect("http://localhost:3000/CompleteOrder?id_order=" + id_order);
            return ApiResponse.<PaymentDTO>builder().result(new PaymentDTO("00", "Success",id_order, "")).build();
        } else {
            orderService.setPaymentStatus(id_order, 2);
            orderService.setOrderStatus(id_order, 4);
            if(platform.equals("web")) response.sendRedirect("http://localhost:3000/ErrorOrder");
            return ApiResponse.<PaymentDTO>builder().code(9999).message("Failed").build();
        }
    }
}