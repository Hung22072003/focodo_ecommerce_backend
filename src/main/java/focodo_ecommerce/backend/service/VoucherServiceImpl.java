package focodo_ecommerce.backend.service;

import focodo_ecommerce.backend.dto.VoucherDTO;
import focodo_ecommerce.backend.entity.Voucher;
import focodo_ecommerce.backend.exception.AppException;
import focodo_ecommerce.backend.exception.ErrorCode;
import focodo_ecommerce.backend.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService{
    private final VoucherRepository voucherRepository;
    @Override
    public List<VoucherDTO> getAllVoucher() {
        return voucherRepository.findAll().stream().filter((voucher) -> !voucher.isExpired()).map(VoucherDTO::new).toList();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public List<VoucherDTO> getAllVoucher(int page, int size) {
        return voucherRepository.findAll(PageRequest.of(page,size)).get().map(VoucherDTO::new).toList();
    }

    @Override
    public VoucherDTO getVoucherById(String id) {
        return new VoucherDTO(voucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND)));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public VoucherDTO saveVoucher(VoucherDTO voucherDTO) {
        LocalDate end_date = LocalDate.parse(voucherDTO.getEnd_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        voucherDTO.setExpired(end_date.isBefore(LocalDate.now()));
        voucherRepository.save(new Voucher(voucherDTO));
        return voucherDTO;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteVoucher(String id) {
        voucherRepository.deleteById(id);
    }


}
