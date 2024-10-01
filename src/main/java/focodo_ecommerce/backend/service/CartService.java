package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CartDTO;
import focodo_ecommerce.backend.model.CartRequest;

import java.util.List;

public interface CartService {
    CartDTO addCart(CartRequest cartRequest);

    List<CartDTO> getCartOfUser();

    CartDTO updateCart(int id, int quantity);

    void deleteCart(int id);
}
