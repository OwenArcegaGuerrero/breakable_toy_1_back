package breakable.toy1.breakable_toy_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import breakable.toy1.breakable_toy_1.dto.ProductCreateDTO;
import breakable.toy1.breakable_toy_1.exception.InvalidProductException;
import breakable.toy1.breakable_toy_1.exception.ProductNotFoundException;
import breakable.toy1.breakable_toy_1.repository.ProductRepository;
import breakable.toy1.breakable_toy_1.service.ProductService;

@SpringBootTest
class BreakableToy1ApplicationTests {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void resetRepository() {
		productRepository.clear();
	}

	@Test
	void shouldCreateProduct() {
		ProductCreateDTO dto = new ProductCreateDTO();
		dto.setName("Tomato");
		dto.setCategory("Fruit");
		dto.setUnitPrice(10.0);
		dto.setQuantityInStock(10L);
		dto.setExpirationDate(LocalDate.now());

		var product = productService.createProduct(dto);
		assertThat(product).isNotNull();
		assertThat(product.getId()).isNotNull();
		assertThat(product.getName()).isEqualTo("Tomato");
		assertThat(product.getCategory()).isEqualTo("Fruit");
		assertThat(product.getUnitPrice()).isEqualTo(10.0);
		assertThat(product.getQuantityInStock()).isEqualTo(10L);
		assertThat(product.getExpirationDate()).isEqualTo(LocalDate.now());

		// Test validation
		var invalidDto = new ProductCreateDTO();
		assertThrows(InvalidProductException.class, () -> productService.createProduct(invalidDto));

		invalidDto.setName("a".repeat(121));
		invalidDto.setCategory("Fruit");
		invalidDto.setUnitPrice(10.0);
		invalidDto.setQuantityInStock(10L);
		assertThrows(InvalidProductException.class, () -> productService.createProduct(invalidDto));

		invalidDto.setName("");
		assertThrows(InvalidProductException.class, () -> productService.createProduct(invalidDto));

		invalidDto.setName("Tomato");
		invalidDto.setCategory("");
		assertThrows(InvalidProductException.class, () -> productService.createProduct(invalidDto));

		invalidDto.setCategory("Fruit");
		invalidDto.setUnitPrice(0.0);
		assertThrows(InvalidProductException.class, () -> productService.createProduct(invalidDto));

		invalidDto.setUnitPrice(-10.0);
		assertThrows(InvalidProductException.class, () -> productService.createProduct(invalidDto));

		invalidDto.setUnitPrice(null);
		assertThrows(InvalidProductException.class, () -> productService.createProduct(invalidDto));
	}

	@Test
	void shouldSaveAndRetrieveProduct() {
		ProductCreateDTO dto = new ProductCreateDTO();
		dto.setName("Tomato");
		dto.setCategory("Fruit");
		dto.setUnitPrice(10.0);
		dto.setQuantityInStock(10L);
		dto.setExpirationDate(LocalDate.now());

		var savedProduct = productService.createProduct(dto);
		var retrievedProduct = productService.getProductById(savedProduct.getId());

		assertThat(retrievedProduct).isNotNull();
		assertThat(retrievedProduct.getId()).isEqualTo(savedProduct.getId());
		assertThat(retrievedProduct.getName()).isEqualTo("Tomato");
		assertThat(retrievedProduct.getCategory()).isEqualTo("Fruit");
		assertThat(retrievedProduct.getUnitPrice()).isEqualTo(10.0);
		assertThat(retrievedProduct.getQuantityInStock()).isEqualTo(10L);

		assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
	}

	@Test
	void shouldUpdateProduct() {
		ProductCreateDTO createDto = new ProductCreateDTO();
		createDto.setName("Tomato");
		createDto.setCategory("Fruit");
		createDto.setUnitPrice(10.0);
		createDto.setQuantityInStock(10L);
		createDto.setExpirationDate(LocalDate.now());

		var savedProduct = productService.createProduct(createDto);

		ProductCreateDTO updateDto = new ProductCreateDTO();
		updateDto.setName("Updated Tomato");
		updateDto.setCategory("Updated Category");
		updateDto.setUnitPrice(15.0);
		updateDto.setQuantityInStock(20L);
		updateDto.setExpirationDate(LocalDate.now().plusDays(1));

		var updatedProduct = productService.updateProduct(savedProduct.getId(), updateDto);

		assertThat(updatedProduct.getName()).isEqualTo("Updated Tomato");
		assertThat(updatedProduct.getCategory()).isEqualTo("Updated Category");
		assertThat(updatedProduct.getUnitPrice()).isEqualTo(15.0);
		assertThat(updatedProduct.getQuantityInStock()).isEqualTo(20L);

		assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(999L, updateDto));
	}

	@Test
	void shouldDeleteProduct() {
		ProductCreateDTO dto = new ProductCreateDTO();
		dto.setName("Tomato");
		dto.setCategory("Fruit");
		dto.setUnitPrice(10.0);
		dto.setQuantityInStock(10L);
		dto.setExpirationDate(LocalDate.now());

		var savedProduct = productService.createProduct(dto);
		productService.deleteProduct(savedProduct.getId());

		assertThrows(ProductNotFoundException.class, () -> productService.getProductById(savedProduct.getId()));
		assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(999L));
	}
}
