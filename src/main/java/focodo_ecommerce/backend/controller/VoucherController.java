package focodo_ecommerce.backend.controller;
import focodo_ecommerce.backend.dto.VoucherDTO;
import focodo_ecommerce.backend.model.ApiResponse;
import focodo_ecommerce.backend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
@CrossOrigin
public class VoucherController {

    private final VoucherService voucherService;
    @GetMapping("/getAllVouchers")
    public ApiResponse<List<VoucherDTO>> getAllVoucher() {
        return ApiResponse.<List<VoucherDTO>>builder().result(voucherService.getAllVoucher()).build();
    }

    @GetMapping("")
    public ApiResponse<List<VoucherDTO>> getAllVoucher(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<List<VoucherDTO>>builder().result(voucherService.getAllVoucher(page, size)).build();
    }

    @GetMapping("/all")
    public ApiResponse<List<VoucherDTO>> getAllVoucherNotPaginated() {
        return ApiResponse.<List<VoucherDTO>>builder().result(voucherService.getAllVoucherNotPaginated()).build();
    }
    @GetMapping("/{id}")
    public ApiResponse<VoucherDTO> getVoucherById(
            @PathVariable("id") String id
    ) {
        return ApiResponse.<VoucherDTO>builder().result(voucherService.getVoucherById(id)).build();
    }
    @PostMapping("/save")
    public ApiResponse<VoucherDTO> saveVoucher(
            @RequestBody VoucherDTO voucherDTO
    ) {
        return ApiResponse.<VoucherDTO>builder().result(voucherService.saveVoucher(voucherDTO)).build();
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteVoucher(@PathVariable String id) {
        voucherService.deleteVoucher(id);
        return ApiResponse.<String>builder().result("Delete voucher successfully").build();
    }

    @GetMapping("/checkVoucher")
    public ApiResponse<Boolean> checkVoucher(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "total") long total
    ) {
        return ApiResponse.<Boolean>builder().result(voucherService.checkVoucher(id, total)).build();
    }
}
