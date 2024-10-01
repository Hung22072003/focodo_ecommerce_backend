package focodo_ecommerce.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Pagination {
    private long total_records;
    private int current_page;
    private int total_pages;

    public Pagination(long totalElements, int totalPages, int number) {
        this.total_records = totalElements;
        this.total_pages = totalPages;
        this.current_page = number;
    }
}
