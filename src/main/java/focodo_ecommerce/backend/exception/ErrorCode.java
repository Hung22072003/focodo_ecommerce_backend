package focodo_ecommerce.backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    VOUCHER_NOT_FOUND(1001, "Voucher not found"),
    DISCOUNT_INVALID(1002, "Discount value must be less than 1"),
    PRODUCT_NOT_FOUND(1003, "Product not found"),
    PRODUCT_EXIST(1004, "Product already exists"),
    CATEGORY_NOT_FOUND(1005, "Category not found"),
    USER_EXIST(1006, "User already exists"),
    USER_NOT_FOUND(1007, "User not found"),
    TOKEN_EXPIRED(1008, "Token is expired"),
    REVIEW_NOT_FOUND(1009, "Review not found"),
    OTP_INVALID(1010, "Otp invalid for email"),
    OTP_EXPIRED(1011, "Otp is expired"),
    OLD_PASSWORD_NOT_CORRECT(1012, "Old password is not correct"),
    PASSWORD_INVALID(1013, "Password has length at least 6"),
    CART_NOT_FOUND(1014, "Cart not found"),
    PAYMENT_METHOD_NOT_FOUND(1015, "Payment method not found"),
    ORDER_NOT_FOUND(1016, "Order not found"),
    ORDER_STATUS_NOT_FOUND(1017, "Order status not found"),
    NOTIFICATION_NOT_FOUND(1018, "Notification not found"),
    CATEGORY_EXIST(1019, "Category already exists"),
    PAYMENT_STATUS_NOT_FOUND(1020, "Payment status not found"),
    PHONE_EXIST(1021, "Phone has been already used");
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
