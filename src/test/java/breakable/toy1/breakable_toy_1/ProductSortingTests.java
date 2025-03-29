package breakable.toy1.breakable_toy_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit tests for product sorting functionality
 */
public class ProductSortingTests {
    private List<Product> products;

    /**
     * Sets up test data before each test.
     * Creates products with various properties to test different sorting scenarios.
     */
    @BeforeEach
    void setUp() throws Exception {
        products = new ArrayList<>();

        // Products with different names but same category
        products.add(new Product("Banana", "Fruit", 2.0, LocalDate.now().plusDays(5), 100L,
                LocalDate.now(), LocalDate.now()));
        products.add(new Product("Apple", "Fruit", 1.5, LocalDate.now().plusDays(3), 50L,
                LocalDate.now(), LocalDate.now()));

        // Products with null values
        products.add(new Product("Orange", "Fruit", 3.0, null, 75L,
                LocalDate.now(), LocalDate.now()));

        // Products with different categories
        products.add(new Product("Carrot", "Vegetable", 1.0, LocalDate.now().plusDays(7), 200L,
                LocalDate.now(), LocalDate.now()));
    }

    /**
     * Tests sorting by name in ascending order
     */
    @Test
    @DisplayName("Should sort products by name ascending")
    void shouldSortByNameAscending() {
        ProductComparator comparator = new ProductComparator("name", SortOrder.ASC, null, null);
        products.sort(comparator);

        assertThat(products).extracting("name")
                .containsExactly("Apple", "Banana", "Carrot", "Orange");
    }

    /**
     * Tests sorting by name in descending order
     */
    @Test
    @DisplayName("Should sort products by name descending")
    void shouldSortByNameDescending() {
        ProductComparator comparator = new ProductComparator("name", SortOrder.DESC, null, null);
        products.sort(comparator);

        assertThat(products).extracting("name")
                .containsExactly("Orange", "Carrot", "Banana", "Apple");
    }

    /**
     * Tests sorting by category and then by price
     */
    @Test
    @DisplayName("Should sort products by category and price")
    void shouldSortByCategoryAndPrice() {
        ProductComparator comparator = new ProductComparator("category", SortOrder.ASC, "price", SortOrder.DESC);
        products.sort(comparator);

        assertThat(products).extracting("category", "unitPrice")
                .containsExactly(
                        tuple("Fruit", 3.0),
                        tuple("Fruit", 2.0),
                        tuple("Fruit", 1.5),
                        tuple("Vegetable", 1.0));
    }

    /**
     * Tests sorting with null expiration dates
     */
    @Test
    @DisplayName("Should handle null expiration dates in sorting")
    void shouldHandleNullExpirationDates() {
        ProductComparator comparator = new ProductComparator("expiration", SortOrder.ASC, null, null);
        products.sort(comparator);

        assertThat(products).extracting("name")
                .satisfies(names -> {
                    assertThat(names.get(0)).isEqualTo("Apple"); // Earliest expiration
                    assertThat(names.get(names.size() - 1)).isEqualTo("Orange"); // Null expiration
                });
    }

    /**
     * Tests sorting by stock quantity
     */
    @Test
    @DisplayName("Should sort products by stock quantity")
    void shouldSortByStock() {
        ProductComparator comparator = new ProductComparator("stock", SortOrder.ASC, null, null);
        products.sort(comparator);

        assertThat(products).extracting("quantityInStock")
                .containsExactly(50L, 75L, 100L, 200L);
    }

    /**
     * Tests SortOrder enum functionality
     */
    @Test
    @DisplayName("Should handle different sort order inputs")
    void shouldHandleSortOrderInputs() {
        assertThat(SortOrder.fromString("ASC")).isEqualTo(SortOrder.ASC);
        assertThat(SortOrder.fromString("DESC")).isEqualTo(SortOrder.DESC);
        assertThat(SortOrder.fromString(null)).isEqualTo(SortOrder.ASC);
        assertThat(SortOrder.fromString("INVALID")).isEqualTo(SortOrder.ASC);
    }

    /**
     * Tests handling of invalid sort fields
     */
    @Test
    @DisplayName("Should handle invalid sort fields")
    void shouldHandleInvalidSortFields() {
        ProductComparator comparator = new ProductComparator("invalid_field", SortOrder.ASC, null, null);
        products.sort(comparator);

        // Should default to sorting by name
        assertThat(products).extracting("name")
                .containsExactly("Apple", "Banana", "Carrot", "Orange");
    }
}