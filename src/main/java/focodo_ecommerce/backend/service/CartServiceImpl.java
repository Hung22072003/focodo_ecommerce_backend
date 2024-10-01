package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.CartDTO;
import focodo_ecommerce.backend.entity.Cart;
import focodo_ecommerce.backend.entity.Product;
import focodo_ecommerce.backend.entity.User;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.model.CartRequest;
import focodo_ecommerce.backend.repository.CartRepository;
import focodo_ecommerce.backend.repository.ProductRepository;
import focodo_ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Override
    public CartDTO addCart(CartRequest cartRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(cartRequest.getId_product()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        Cart newCart = new Cart(cartRequest.getQuantity(), user, product);
        return new CartDTO(cartRepository.save(newCart));
    }

    @Override
    public List<CartDTO> getCartOfUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getCarts().stream().map(CartDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartDTO updateCart(int id, int quantity) {
        Cart foundCart = cartRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        foundCart.setQuantity(quantity);
        return new CartDTO(foundCart);
    }

    @Override
    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }
}
