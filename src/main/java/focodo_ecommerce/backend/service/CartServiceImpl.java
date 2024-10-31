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
    @Transactional
    public void addCart(CartRequest cartRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Cart> existCarts = user.getCarts();
        Product product = productRepository.findById(cartRequest.getId_product()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        int quantity = product.getPackage_quantity() * cartRequest.getQuantity();
        if(product.getQuantity() < quantity) throw new RuntimeException("Product is not enough quantity");
        boolean isExistProduct = false;
        for(Cart cart : existCarts) {
            if(cart.getProduct().equals(product)) {
                isExistProduct = true;
                break;
            }
        }
        if(isExistProduct) {
            user.setCarts(existCarts.stream().map((cart) -> {
                if(cart.getProduct().equals(product)) cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
                return cart;
            }).toList());
        }
        else {
            Cart newCart = new Cart(cartRequest.getQuantity(), user, product);
            cartRepository.save(newCart);
        }
        product.setQuantity(product.getQuantity() - quantity);
    }

    @Override
    public List<CartDTO> getCartOfUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getCarts().stream().map(CartDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartDTO updateCart(int id) {
        Cart foundCart = cartRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        foundCart.setCheck(!foundCart.getCheck());
        return new CartDTO(foundCart);
    }

    @Override
    @Transactional
    public void deleteCart(int id) {
        Cart foundCart = cartRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Product product = foundCart.getProduct();
        product.setQuantity(product.getQuantity() + product.getPackage_quantity() * foundCart.getQuantity());
        cartRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CartDTO increaseQuantityCart(int id) {
        Cart foundCart = cartRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Product product = foundCart.getProduct();
        if(product.getQuantity() < product.getPackage_quantity()) throw new RuntimeException("Product is not enough quantity");
        foundCart.setQuantity(foundCart.getQuantity() + 1);
        product.setQuantity(product.getQuantity() - product.getPackage_quantity());
        return new CartDTO(foundCart);
    }

    @Override
    @Transactional
    public CartDTO decreaseQuantityCart(int id) {
        Cart foundCart = cartRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Product product = foundCart.getProduct();
        foundCart.setQuantity(foundCart.getQuantity() - 1);
        product.setQuantity(product.getQuantity() + product.getPackage_quantity());
        return new CartDTO(foundCart);
    }

    @Override
    @Transactional
    public CartDTO updateQuantityCart(int id, int quantity) {
        Cart foundCart = cartRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Product product = foundCart.getProduct();
        if(product.getQuantity() < product.getPackage_quantity() * quantity) throw new RuntimeException("Product is not enough quantity");
        product.setQuantity(product.getQuantity() + product.getPackage_quantity()*(foundCart.getQuantity() - quantity));
        foundCart.setQuantity(quantity);
        return new CartDTO(foundCart);
    }

    @Override
    public Integer getNumberOfCart() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getCarts().size();
    }

}
