package breakable.toy1.breakable_toy_1.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ProductCreateDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 120, message = "Name must be less than 120 characters")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @Positive(message = "Price must be positive")
    private Double unitPrice;

    private LocalDate expirationDate;

    @PositiveOrZero(message = "Quantity must not be negative")
    private Long quantityInStock;
}