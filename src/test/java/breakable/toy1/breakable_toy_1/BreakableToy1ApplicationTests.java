package breakable.toy1.breakable_toy_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BreakableToy1ApplicationTests {

	@Test
	void ShouldCreateAProductLocally() throws Exception {
		Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		assertThat(product).isNotNull();

		String longName = "a".repeat(121);
		Exception nameTooLong = assertThrows(Exception.class, () -> {
			new Product(longName, "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid name").isEqualTo(nameTooLong.getMessage());

		Exception shortName = assertThrows(Exception.class, () -> {
			new Product("", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid name").isEqualTo(shortName.getMessage());

		Exception nullName = assertThrows(Exception.class, () -> {
			new Product(null, "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid name").isEqualTo(nullName.getMessage());

		Exception shortCategory = assertThrows(Exception.class, () -> {
			new Product("Tomato", "", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid category").isEqualTo(shortCategory.getMessage());

		Exception nullCategory = assertThrows(Exception.class, () -> {
			new Product("Tomato", null, 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid category").isEqualTo(nullCategory.getMessage());

		Exception zeroUnitPrice = assertThrows(Exception.class, () -> {
			new Product("Tomato", "Fruit", 0.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid Unit Price").isEqualTo(zeroUnitPrice.getMessage());

		Exception negativeUnitPrice = assertThrows(Exception.class, () -> {
			new Product("Tomato", "Fruit", -10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid Unit Price").isEqualTo(negativeUnitPrice.getMessage());

		Exception nullUnitPrice = assertThrows(Exception.class, () -> {
			new Product("Tomato", "Fruit", null, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		});
		assertThat("Invalid Unit Price").isEqualTo(nullUnitPrice.getMessage());
	}

}
