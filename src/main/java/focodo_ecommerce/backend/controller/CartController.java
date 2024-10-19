package focodo_ecommerce.backend.controller;

import focodo_ecommerce.backend.dto.CartDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.model.CartRequest;
import focodo_ecommerce.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@CrossOrigin
public class CartController {
    private final CartService cartService;

    @GetMapping("/getCartOfUser")
    public ApiResponse<List<CartDTO>> getCartOfUser() {
        return ApiResponse.<List<CartDTO>>builder().result(cartService.getCartOfUser()).build();
    }
    @PostMapping("/addCart")
    public void addCart(@RequestBody CartRequest cartRequest) {
        cartService.addCart(cartRequest);
    }


    @PutMapping("/updateCart/{id}")
    public ApiResponse<CartDTO> updateCart(
            @PathVariable("id") int id
    ) {
        return ApiResponse.<CartDTO>builder().result(cartService.updateCart(id)).build();
    }


    @PutMapping("/updateQuantityCart/{id}")
    public ApiResponse<CartDTO> updateQuantityCart(
            @PathVariable("id") int id,
            @RequestParam("quantity") int quantity
    ) {
        return ApiResponse.<CartDTO>builder().result(cartService.updateQuantityCart(id, quantity)).build();
    }
    @PutMapping("/increaseQuantityCart/{id}")
    public ApiResponse<CartDTO> increaseQuantityCart(
        @PathVariable("id") int id
    ) {
        return ApiResponse.<CartDTO>builder().result(cartService.increaseQuantityCart(id)).build();
    }

    @PutMapping("/decreaseQuantityCart/{id}")
    public ApiResponse<CartDTO> decreaseQuantityCart(
            @PathVariable("id") int id
    ) {
        return ApiResponse.<CartDTO>builder().result(cartService.decreaseQuantityCart(id)).build();
    }

    @DeleteMapping("/deleteCart/{id}")
    public void deleteCart(
            @PathVariable("id") int id
    ) {
        cartService.deleteCart(id);
    }
}
