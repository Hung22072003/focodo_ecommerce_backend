package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.config.VNPAYConfig;
import focodo_ecommerce.backend.dto.PaymentDTO;
import focodo_ecommerce.backend.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;
    private final VNPAYConfig vnpayConfig;
    public PaymentDTO createVnPayPayment(HttpServletRequest request, Long amount, String id_order, String platform) {
        amount = amount * 100L;
        Map<String, String> vnpParamsMap = vnpayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_TxnRef", id_order);
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl + "?platform=" + platform);
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return new PaymentDTO("ok", "success", id_order, paymentUrl);
    }
}
