package breakable.toy1.breakable_toy_1;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

import breakable.toy1.breakable_toy_1.dto.ProductCreateDTO;
import breakable.toy1.breakable_toy_1.repository.ProductRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=9091")
public class BreakableToy1ApplicationAPITests {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	ProductRepository productRepository;

	private static final String BASE_URL = "/api/v1/products";

	/**
	 * Sets up test data before each test.
	 * Creates 5 products with different properties to test various scenarios.
	 */
	@BeforeEach
	public void setUp() {
		// Clear all existing data
		productRepository.clear();

		// Create test products
		createTestProduct("Product A", "Category 1", 10.0, null, 50L);
		createTestProduct("Product B", "Category 2", 12.0, LocalDate.now(), 50L);
		createTestProduct("Product C", "Category 1", 15.0, null, 50L);
		createTestProduct("Product D", "Category 2", 8.0, LocalDate.of(2025, 6, 10), 50L);
		createTestProduct("Product E", "Category 3", 20.0, null, 50L);
	}

	private void createTestProduct(String name, String category, Double price, LocalDate expDate, Long stock) {
		ProductCreateDTO dto = new ProductCreateDTO();
		dto.setName(name);
		dto.setCategory(category);
		dto.setUnitPrice(price);
		dto.setExpirationDate(expDate);
		dto.setQuantityInStock(stock);
		restTemplate.postForEntity(BASE_URL, dto, String.class);
	}

	/**
	 * Tests the retrieval of a product by its ID.
	 * Verifies all product fields are correctly returned.
	 */
	@Test
	@DisplayName("Should successfully retrieve a product by ID")
	void shouldGetProductById() {
		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id).isNotNull();

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Product A");

		String category = documentContext.read("$.category");
		assertThat(category).isEqualTo("Category 1");

		Number price = documentContext.read("$.unitPrice");
		assertThat(price).isEqualTo(10.0);

		String date = documentContext.read("$.expirationDate");
		assertThat(date).isNull();

		Number stock = documentContext.read("$.quantityInStock");
		assertThat(stock).isEqualTo(50);
	}

	/**
	 * Tests the pagination functionality of the products endpoint.
	 * Verifies correct page size, total elements, and page information.
	 */
	@Test
	@DisplayName("Should return paginated products")
	void shouldReturnPaginatedProducts() {
		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "?page=0&size=2", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray content = documentContext.read("$.content");
		assertThat(content.size()).isEqualTo(2);

		int pageNumber = documentContext.read("$.pageNumber");
		assertThat(pageNumber).isEqualTo(0);

		int pageSize = documentContext.read("$.pageSize");
		assertThat(pageSize).isEqualTo(2);

		int totalPages = documentContext.read("$.totalPages");
		assertThat(totalPages).isEqualTo(3);

		Number totalElements = documentContext.read("$.totalElements");
		assertThat(totalElements.intValue()).isEqualTo(5);

		boolean isFirst = documentContext.read("$.first");
		assertThat(isFirst).isTrue();

		boolean isLast = documentContext.read("$.last");
		assertThat(isLast).isFalse();
	}

	/**
	 * Tests the sorting functionality with a single sort field.
	 * Verifies products are correctly sorted by name in ascending order.
	 */
	@Test
	@DisplayName("Should return products sorted by single field")
	void shouldReturnProductsSortedBySingleField() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				BASE_URL + "?sortBy=name&sortOrder=asc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray names = documentContext.read("$.content[*].name");
		assertThat(names).containsExactly(
				"Product A", "Product B", "Product C", "Product D", "Product E");
	}

	/**
	 * Tests the sorting functionality with two sort fields.
	 * Verifies products are correctly sorted by category (primary) and price
	 * (secondary).
	 */
	@Test
	@DisplayName("Should return products sorted by multiple fields")
	void shouldReturnProductsSortedByMultipleFields() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				BASE_URL + "?sortBy=category&sortOrder=asc&secondarySortBy=price&secondarySortOrder=desc",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray categories = documentContext.read("$.content[*].category");
		JSONArray prices = documentContext.read("$.content[*].unitPrice");

		assertThat(categories).containsExactly(
				"Category 1", "Category 1", "Category 2", "Category 2", "Category 3");
		assertThat(prices).containsExactly(15.0, 10.0, 12.0, 8.0, 20.0);
	}

	@Test
	void shouldNotReturnProductWithoutId() {
		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/0", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldCreateProduct() {
		ProductCreateDTO dto = new ProductCreateDTO();
		dto.setName("Product F");
		dto.setCategory("Category 3");
		dto.setUnitPrice(5.0);
		dto.setQuantityInStock(20L);

		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, dto, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewProduct = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewProduct, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		assertThat((String) documentContext.read("$.name")).isEqualTo("Product F");
		assertThat((String) documentContext.read("$.category")).isEqualTo("Category 3");
		assertThat((Number) documentContext.read("$.unitPrice")).isEqualTo(5.0);
		assertThat((Number) documentContext.read("$.quantityInStock")).isEqualTo(20L);
	}

	@Test
	void shouldNotCreateProductWithEmptyBody() {
		ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, null, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void shouldUpdateProduct() {
		ProductCreateDTO dto = new ProductCreateDTO();
		dto.setName("Updated Product");
		dto.setCategory("Updated Category");
		dto.setUnitPrice(15.0);
		dto.setQuantityInStock(30L);
		dto.setExpirationDate(LocalDate.now().plusDays(10));

		HttpEntity<ProductCreateDTO> request = new HttpEntity<>(dto);
		ResponseEntity<String> response = restTemplate.exchange(
				BASE_URL + "/1", HttpMethod.PUT, request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		assertThat((String) documentContext.read("$.name")).isEqualTo("Updated Product");
		assertThat((String) documentContext.read("$.category")).isEqualTo("Updated Category");
		assertThat((Number) documentContext.read("$.unitPrice")).isEqualTo(15.0);
		assertThat((Number) documentContext.read("$.quantityInStock")).isEqualTo(30L);
	}

	@Test
	void shouldDeleteProduct() {
		ResponseEntity<Void> response = restTemplate.exchange(
				BASE_URL + "/1", HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate.getForEntity(BASE_URL + "/1", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldHandleInvalidProductId() {
		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/999", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}