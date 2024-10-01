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
public class CartController {
    private final CartService cartService;

    @GetMapping("/getCartOfUser")
    public ApiResponse<List<CartDTO>> getCartOfUser() {
        return ApiResponse.<List<CartDTO>>builder().result(cartService.getCartOfUser()).build();
    }
    @PostMapping("/addCart")
    public ApiResponse<CartDTO> addCart(@RequestBody CartRequest cartRequest) {
        return ApiResponse.<CartDTO>builder().result(cartService.addCart(cartRequest)).build();
    }

    @PutMapping("/updateCart/{id}")
    public ApiResponse<CartDTO> updateCart(
        @PathVariable("id") int id,
        @RequestParam("quantity") int quantity
    ) {
        return ApiResponse.<CartDTO>builder().result(cartService.updateCart(id, quantity)).build();
    }

    @DeleteMapping("/deleteCart/{id}")
    public void deleteCart(
            @PathVariable("id") int id
    ) {
        cartService.deleteCart(id);
    }
}
