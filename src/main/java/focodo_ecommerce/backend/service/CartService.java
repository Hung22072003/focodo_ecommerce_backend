package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CartDTO;
import focodo_ecommerce.backend.model.CartRequest;

import java.util.List;

public interface CartService {
    void addCart(CartRequest cartRequest);

    List<CartDTO> getCartOfUser();

    CartDTO updateCart(int id);

    void deleteCart(int id);

    CartDTO increaseQuantityCart(int id);

    CartDTO decreaseQuantityCart(int id);

    CartDTO updateQuantityCart(int id, int quantity);
}
