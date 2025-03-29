package breakable.toy1.breakable_toy_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import breakable.toy1.breakable_toy_1.dto.ProductCreateDTO;
import breakable.toy1.breakable_toy_1.dto.ProductDTO;
import breakable.toy1.breakable_toy_1.dto.ProductSearchCriteria;
import breakable.toy1.breakable_toy_1.dto.PaginationRequest;
import breakable.toy1.breakable_toy_1.model.PageResponse;
import breakable.toy1.breakable_toy_1.service.ProductService;

@SpringBootTest
public class ProductSortingTests {

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // Create test products with different properties
        createTestProduct("Apple", "Fruit", 2.0, LocalDate.now().plusDays(5), 100L);
        createTestProduct("Banana", "Fruit", 1.5, LocalDate.now().plusDays(3), 150L);
        createTestProduct("Carrot", "Vegetable", 1.0, LocalDate.now().plusDays(7), 50L);
        createTestProduct("Dragonfruit", "Fruit", 5.0, LocalDate.now().plusDays(10), 200L);
        createTestProduct("Eggplant", "Vegetable", 3.0, LocalDate.now().plusDays(4), 75L);
    }

    private void createTestProduct(String name, String category, Double price, LocalDate expDate, Long stock) {
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName(name);
        dto.setCategory(category);
        dto.setUnitPrice(price);
        dto.setExpirationDate(expDate);
        dto.setQuantityInStock(stock);
        productService.createProduct(dto);
    }

    @Test
    @DisplayName("Should sort products by name in ascending order")
    void shouldSortByNameAscending() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setSortBy("name");
        criteria.setSortOrder("asc");

        PaginationRequest pagination = new PaginationRequest();
        pagination.setPage(0);
        pagination.setSize(10);

        PageResponse<ProductDTO> response = productService.getAllProducts(criteria, pagination);
        assertThat(response.getContent())
            .extracting("name")
            .containsExactly("Apple", "Banana", "Carrot", "Dragonfruit", "Eggplant");
    }

    @Test
    @DisplayName("Should sort products by price in descending order")
    void shouldSortByPriceDescending() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setSortBy("unitPrice");
        criteria.setSortOrder("desc");

        PaginationRequest pagination = new PaginationRequest();
        pagination.setPage(0);
        pagination.setSize(10);

        PageResponse<ProductDTO> response = productService.getAllProducts(criteria, pagination);
        assertThat(response.getContent())
            .extracting("unitPrice")
            .containsExactly(5.0, 3.0, 2.0, 1.5, 1.0);
    }

    @Test
    @DisplayName("Should sort products by stock quantity in ascending order")
    void shouldSortByStockQuantity() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setSortBy("quantityInStock");
        criteria.setSortOrder("asc");

        PaginationRequest pagination = new PaginationRequest();
        pagination.setPage(0);
        pagination.setSize(10);

        PageResponse<ProductDTO> response = productService.getAllProducts(criteria, pagination);
        assertThat(response.getContent())
            .extracting("quantityInStock")
            .containsExactly(50L, 75L, 100L, 150L, 200L);
    }

    @Test
    @DisplayName("Should handle invalid sort field gracefully")
    void shouldHandleInvalidSortField() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setSortBy("invalidField");
        criteria.setSortOrder("asc");

        PaginationRequest pagination = new PaginationRequest();
        pagination.setPage(0);
        pagination.setSize(10);

        PageResponse<ProductDTO> response = productService.getAllProducts(criteria, pagination);
        assertThat(response.getContent()).isNotEmpty();
    }
}
