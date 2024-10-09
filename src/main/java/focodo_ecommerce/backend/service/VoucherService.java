package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.VoucherDTO;

import java.util.List;

public interface VoucherService {
    List<VoucherDTO> getAllVoucher();

    VoucherDTO saveVoucher(VoucherDTO voucherDTO);

    void deleteVoucher(String id);

    List<VoucherDTO> getAllVoucher(int page, int size);

    VoucherDTO getVoucherById(String id);

    List<VoucherDTO> getAllVoucherNotPaginated();

    Boolean checkVoucher(String id);
}
