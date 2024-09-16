package focodo_ecommerce.backend.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    VOUCHER_EXIST(1001, "Voucher already exists"),
    DISCOUNT_INVALID(1002, "Discount value must be less than 1"),
    PRODUCT_NOT_FOUND(1003, "Product not found"),
    PRODUCT_EXIST(1004, "Product already exists"),
    CATEGORY_NOT_FOUND(1005, "Category not found"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;
}
