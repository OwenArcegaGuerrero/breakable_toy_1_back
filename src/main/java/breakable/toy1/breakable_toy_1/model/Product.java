package breakable.toy1.breakable_toy_1.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Product {
    private Long id = 0L;
    private static Long idCounter = 0L;
    private String name;
    private String category;
    private Double unitPrice;
    private LocalDate expirationDate;
    private Long quantityInStock;
    private LocalDate creationDate;
    private LocalDate updateDate;

    public Product(String name, String category, Double unitPrice, LocalDate expirationDate, Long quantityInStock,
            LocalDate creationDate, LocalDate updateDate) {
        this.id = assignId();

        if (name == null || name.length() <= 0 || name.length() > 120) {
            throw new IllegalArgumentException("Invalid name");
        }
        this.name = name;

        if (category == null || category.length() <= 0) {
            throw new IllegalArgumentException("Invalid category");
        }
        this.category = category;

        if (unitPrice == null || unitPrice <= 0.0) {
            throw new IllegalArgumentException("Invalid Unit Price");
        }
        this.unitPrice = unitPrice;

        this.expirationDate = expirationDate;

        if (quantityInStock == null) {
            throw new IllegalArgumentException("Invalid Quantity In Stock");
        }
        this.quantityInStock = quantityInStock;

        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    private Long assignId() {
        return ++idCounter;
    }

    public static void resetidCounter() {
        idCounter = 0L;
    }

    public Boolean inStock() {
        return this.getQuantityInStock() > 0;
    }
}