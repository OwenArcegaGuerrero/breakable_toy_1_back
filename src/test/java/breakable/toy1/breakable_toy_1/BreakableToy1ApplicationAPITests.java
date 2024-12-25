package breakable.toy1.breakable_toy_1;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=9091")
public class BreakableToy1ApplicationAPITests {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	ProductStorage storage;

	@BeforeEach
	public void setUp() throws Exception {
		storage.clear();

		Product product1 = new Product("Product A", "Category 1", 10.0, null,
				50l, LocalDate.now(), LocalDate.now());
		Product product2 = new Product("Product B", "Category 2", 12.0,
				LocalDate.now(), 50l, LocalDate.now(), LocalDate.now());
		Product product3 = new Product("Product C", "Category 1", 15.0, null,
				50l, LocalDate.now(), LocalDate.now());
		Product product4 = new Product("Product D", "Category 2", 8.0, LocalDate.of(2025, 6, 10),
				50l, LocalDate.now(),
				LocalDate.now());
		Product product5 = new Product("Product E", "Category 3", 20.0, null,
				50l, LocalDate.now(), LocalDate.now());

		storage.saveProduct(product1);
		storage.saveProduct(product2);
		storage.saveProduct(product3);
		storage.saveProduct(product4);
		storage.saveProduct(product5);
	}

	@Test
	void ShouldGetProductById() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity("/products/1", String.class);
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

		Number stock = documentContext.read("quantityInStock");
		assertThat(stock).isEqualTo(50);

		String creationDate = documentContext.read("creationDate");
		assertThat(creationDate).isEqualTo(LocalDate.now().toString());

		String updateDate = documentContext.read("updateDate");
		assertThat(updateDate).isEqualTo(LocalDate.now().toString());
	}

	@Test
	void ShouldNotReturnProductWihtoutId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products/0", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void ShouldCreateProduct() throws Exception {
		Product newProduct = new Product("Product F", "Category 3", 5.0, null, 20l, LocalDate.now(), LocalDate.now());

		ResponseEntity<String> response = restTemplate.postForEntity("/products/create", newProduct, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewProduct = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewProduct, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

		Number id = documentContext.read("$.id");
		assertThat(id).isNotNull();

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Product F");

		String category = documentContext.read("$.category");
		assertThat(category).isEqualTo("Category 3");

		Number price = documentContext.read("$.unitPrice");
		assertThat(price).isEqualTo(5.0);

		String expirationDate = documentContext.read("$.expirationDate");
		assertThat(expirationDate).isEqualTo(null);

		Number stock = documentContext.read("$.quantityInStock");
		assertThat(stock).isEqualTo(20);

		String creationDate = documentContext.read("$.creationDate");
		assertThat(creationDate).isEqualTo(LocalDate.now().toString());

		String updateDate = documentContext.read("$.updateDate");
		assertThat(updateDate).isEqualTo(LocalDate.now().toString());
	}

	@Test
	void ShouldNotCreateProductWithEmptyBody() {
		ResponseEntity<String> response = restTemplate.postForEntity("/products/create", null, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void ShouldReturnAllProducts() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int length = documentContext.read("$.length()");
		assertThat(length).isEqualTo(5);

		JSONArray ids = documentContext.read("$..id");
		assertThat(ids).isNotNull();

		JSONArray names = documentContext.read("$..name");
		assertThat(names).containsExactlyInAnyOrder("Product A", "Product B", "Product C", "Product D", "Product E");

		JSONArray categories = documentContext.read("$..category");
		assertThat(categories).containsExactlyInAnyOrder("Category 1", "Category 2", "Category 1", "Category 2",
				"Category 3");

		JSONArray prices = documentContext.read("$..unitPrice");
		assertThat(prices).containsExactlyInAnyOrder(10.0, 12.0, 15.0, 8.0, 20.0);

		JSONArray expirationDates = documentContext.read("$..expirationDate");
		assertThat(expirationDates).containsExactlyInAnyOrder(null, LocalDate.now().toString(), null,
				LocalDate.of(2025, 6, 10).toString(), null);

		JSONArray stocks = documentContext.read("$..quantityInStock");
		assertThat(stocks).containsExactlyInAnyOrder(50, 50, 50, 50, 50);

		JSONArray creationDates = documentContext.read("$..creationDate");
		assertThat(creationDates).containsExactlyInAnyOrder(LocalDate.now().toString(), LocalDate.now().toString(),
				LocalDate.now().toString(), LocalDate.now().toString(), LocalDate.now().toString());

		JSONArray updateDates = documentContext.read("$..updateDate");
		assertThat(updateDates).containsExactlyInAnyOrder(LocalDate.now().toString(), LocalDate.now().toString(),
				LocalDate.now().toString(), LocalDate.now().toString(), LocalDate.now().toString());
	}

	@Test
	void ShouldUpdateAProductById() throws Exception {
		Product updateProduct = new Product("Product F", "Category 4", 10.0, LocalDate.of(2026, 1, 1), 60l,
				LocalDate.now(), LocalDate.now());
		HttpEntity<Product> request = new HttpEntity<>(updateProduct);

		ResponseEntity<Void> response = restTemplate.exchange("/products/update/1", HttpMethod.PUT, request,
				Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> responseGet = restTemplate.getForEntity("/products/1", String.class);
		assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(responseGet.getBody());
		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(1);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Product F");

		String category = documentContext.read("$.category");
		assertThat(category).isEqualTo("Category 4");

		Number price = documentContext.read("$.unitPrice");
		assertThat(price).isEqualTo(10.0);

		String expirationDate = documentContext.read("$.expirationDate");
		assertThat(expirationDate).isEqualTo(LocalDate.of(2026, 1, 1).toString());

		Number stock = documentContext.read("$.quantityInStock");
		assertThat(stock).isEqualTo(60);

		String creationDate = documentContext.read("$.creationDate");
		assertThat(creationDate).isEqualTo(LocalDate.now().toString());

		String updateDate = documentContext.read("$.updateDate");
		assertThat(updateDate).isEqualTo(LocalDate.now().toString());
	}

	@Test
	void ShouldNotUpdateWithInvalidId() throws Exception {
		Product updateProduct = new Product("Product F", "Category 4", 10.0, LocalDate.of(2026, 1, 1), 60l,
				LocalDate.now(), LocalDate.now());
		HttpEntity<Product> request = new HttpEntity<>(updateProduct);

		ResponseEntity<Void> response = restTemplate.exchange("/products/update/0", HttpMethod.PUT, request,
				Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void ShouldNotUpdateWithInvalidProduct() throws Exception {
		ResponseEntity<Void> response = restTemplate.exchange("/products/update/1", HttpMethod.PUT, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void ShouldDeleteAProductById() {
		ResponseEntity<Void> response = restTemplate.exchange("/products/delete/1", HttpMethod.DELETE, null,
				Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> responseGet = restTemplate.getForEntity("/products/1", String.class);
		assertThat(responseGet.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void ShouldNotDeleteAProductWithInvalidValidId() {
		ResponseEntity<Void> response = restTemplate.exchange("/products/delete/0", HttpMethod.DELETE, null,
				Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void ShouldMarkProductAsOutOfStockWithId() {
		ResponseEntity<Void> response = restTemplate.postForEntity("/products/1/outofstock", null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate.getForEntity("/products/1", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number stock = documentContext.read("$.quantityInStock");
		assertThat(stock).isEqualTo(0);
	}

	@Test
	void ShouldNotMarkProductOtOfStockWithInvalidId() {
		ResponseEntity<Void> response = restTemplate.postForEntity("/products/0/outofstock", null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void ShouldMarkProductInStockById() {
		restTemplate.postForEntity("/products/1/outOfStock", null, Void.class);
		ResponseEntity<Void> response = restTemplate.exchange("/products/1/instock", HttpMethod.PUT, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate.getForEntity("/products/1", String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
		Number stock = documentContext.read("$.quantityInStock");
		assertThat(stock).isEqualTo(10);
	}

	@Test
	void ShouldFilterProductsByName() throws Exception {
		Product newProduct = new Product("Product A", "Category 3", 5.0, null, 20l, LocalDate.now(), LocalDate.now());

		ResponseEntity<String> postResponse = restTemplate.postForEntity("/products/create", newProduct, String.class);
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ResponseEntity<String> response = restTemplate.getForEntity("/products?name=Product A", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray names = documentContext.read("$..name");
		assertThat(names).containsExactlyInAnyOrder("Product A", "Product A");
	}

	@Test
	void ShouldFilterProductsByCategory() {
		ResponseEntity<String> response = restTemplate.getForEntity("/products?category=Category 1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray categories = documentContext.read("$..category");
		assertThat(categories).containsExactlyInAnyOrder("Category 1", "Category 1");
	}

	@Test
	void ShouldFilterProducsByAvailability() {
		restTemplate.postForEntity("/products/1/outofstock", null, Void.class);
		restTemplate.postForEntity("/products/2/outofstock", null, Void.class);

		ResponseEntity<String> response = restTemplate.getForEntity("/products?availability=false", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray stocksFalse = documentContext.read("$..quantityInStock");
		assertThat(stocksFalse).containsExactlyInAnyOrder(0, 0);

		response = restTemplate.getForEntity("/products?availability=true", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		documentContext = JsonPath.parse(response.getBody());
		JSONArray stocksTrue = documentContext.read("$..quantityInStock");
		assertThat(stocksTrue).containsExactlyInAnyOrder(50, 50, 50);
	}

	@Test
	void ShouldFilterByMultipleFields() throws Exception {
		restTemplate.postForEntity("/products/1/outofstock", null, Void.class);
		restTemplate.exchange("/products/update/2", HttpMethod.PUT,
				new HttpEntity<>(new Product("Product E", "Category 3", 5.0, null, 10l, null, null)), Void.class);

		ResponseEntity<String> response = restTemplate.getForEntity("/products?name=Product E&category=Category 3",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray names = documentContext.read("$..name");
		assertThat(names).containsExactlyInAnyOrder("Product E", "Product E");

		JSONArray categories = documentContext.read("$..category");
		assertThat(categories).containsExactlyInAnyOrder("Category 3", "Category 3");

		response = restTemplate.getForEntity("/products?availability=true&category=Category 2", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		documentContext = JsonPath.parse(response.getBody());
		JSONArray stocks = documentContext.read("$..quantityInStock");
		assertThat(stocks).containsExactlyInAnyOrder(50);

		categories = documentContext.read("$..category");
		assertThat(categories).containsExactlyInAnyOrder("Category 2");
	}
}