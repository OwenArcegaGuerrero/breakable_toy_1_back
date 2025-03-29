package breakable.toy1.breakable_toy_1.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private Double unitPrice;
    private LocalDate expirationDate;
    private Long quantityInStock;
    private LocalDate creationDate;
    private LocalDate updateDate;
    private boolean inStock;
}