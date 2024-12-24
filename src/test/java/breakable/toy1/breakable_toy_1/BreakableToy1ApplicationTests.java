package breakable.toy1.breakable_toy_1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
//import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BreakableToy1ApplicationTests {

	@BeforeEach
	void resestIdCounter() throws Exception{
		Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		product.resetidCounter();
	}

	@Test
	void ShouldCreateAProductLocally() throws Exception {
		Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		assertThat(product).isNotNull();

		assertThat(product.getId()).isEqualTo(1l);

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

	@Test
	void ShouldSaveProductLocally() throws Exception{
		Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		Product product2 = new Product("Orange", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());

		ProductStorage storage = new ProductStorage();

		storage.saveProduct(product);
		storage.saveProduct(product2);
		assertThat(storage.getSize()).isEqualTo(2);

		Exception nullSaveProduct = assertThrows(Exception.class, () -> {
			storage.saveProduct(null);
		});
		assertThat("Invalid Product").isEqualTo(nullSaveProduct.getMessage());
	}

	@Test
	void ShouldReturnProductByIdLocally() throws Exception{
		Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		Product product2 = new Product("Orange", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());

		ProductStorage storage = new ProductStorage();
		
		storage.saveProduct(product);
		storage.saveProduct(product2);

		assertThat(storage.getProductById(1l)).isEqualTo(product);
		assertThat(storage.getProductById(2l)).isEqualTo(product2);
		assertThat(storage.getProductById(3l)).isEqualTo(null);
	}

	@Test
	void ShouldDeleteProductByIdLocally() throws Exception{
		Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		ProductStorage storage = new ProductStorage();

		storage.saveProduct(product);
		storage.deleteProduct(1l);
		assertThat(storage.getSize()).isEqualTo(0);

		Exception inexistentId = assertThrows(Exception.class, () -> {
			storage.deleteProduct(9999l);
		});
		assertThat("Invalid id").isEqualTo(inexistentId.getMessage());

		Exception nullId = assertThrows(Exception.class, () -> {
			storage.deleteProduct(null);
		});
		assertThat("Invalid id").isEqualTo(nullId.getMessage());
	}

	@Test
	void ShouldUpdateProductLocally() throws Exception{
		Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
		ProductStorage storage = new ProductStorage();
		storage.saveProduct(product);

		Product product2 = new Product("Orange", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());

		storage.updateProductById(product2, product.getId());
		assertThat(storage.getProductById(product2.getId())).isEqualTo(product2);

		Exception inexistentId = assertThrows(Exception.class, () -> {
			storage.updateProductById(product2, 999l);
		});
		assertThat("Invalid id").isEqualTo(inexistentId.getMessage());

		Exception nullId = assertThrows(Exception.class, () -> {
			storage.updateProductById(product2, null);
		});
		assertThat("Invalid id").isEqualTo(nullId.getMessage());

		Exception nullProduct = assertThrows(Exception.class, () -> {
			storage.updateProductById(null, 1l);
		});
		assertThat("Invalid Product").isEqualTo(nullProduct.getMessage());
	}
/* 	@Test
    void ShouldSortProductsLocally() throws Exception{
        Product product = new Product("Tomato", "Fruit", 10.0, LocalDate.now(), 10l, LocalDate.now(), LocalDate.now());
        Product product2 = new Product("Pear", "Fruit", 12.0, LocalDate.of(2020,1,1), 15l, LocalDate.now(), LocalDate.now());
        Product product3 = new Product("Chair", "Furniture", 200.0, null, 20l, LocalDate.now(), LocalDate.now());

        ProductStorage storage = new ProductStorage();
        storage.saveProduct(product);
        storage.saveProduct(product2);
        storage.saveProduct(product3);

        storage.sortByName();
		Iterator<Product> nameIterator = storage.Iterator();
        String[] sortedNames = {"Chair", "Pear", "Tomato"};
		int indexNames = 0;

        while(nameIterator.hasNext()){
			assertThat(nameIterator.next().getName()).isEqualTo(sortedNames[indexNames]);
			indexNames++;
		}

		storage.sortByNameReverse();
		Iterator<Product> reverseNameIterator = storage.Iterator();
		String[] reverseSortedNames = {"Tomato","Pear","Chair"};
		int reverseIndexNames = 0;
		while(reverseNameIterator.hasNext()){
			assertThat(reverseNameIterator.next().getName()).isEqualTo(reverseSortedNames[reverseIndexNames]);
			reverseIndexNames++;
		}

		storage.sortByCategory();
		Iterator<Product> categoryIterator = storage.Iterator();
		String[] sortedCategories = {"Fruit","Fruit","Furniture"};
		int indexCategories = 0;
		while (categoryIterator.hasNext()) {
			assertThat(categoryIterator.next().getCategory()).isEqualTo(sortedCategories[indexCategories]);
			indexCategories++;
		}

		storage.sortByCategoryReverse();
		Iterator<Product> reverseCategoryIterator = storage.Iterator();
		String[] reverseSortedCategories = {"Furniture","Fruit","Fruit"};
		int reverseIndexCategories = 0;
		while (reverseCategoryIterator.hasNext()) {
			assertThat(reverseCategoryIterator.next().getCategory()).isEqualTo(reverseSortedCategories[reverseIndexCategories]);
			reverseIndexCategories++;
		}

		storage.sortByPrice();
		Iterator<Product> priceIterator = storage.Iterator();
		Double[] sortedPrices = {10.0, 12.0, 200.0};
		int indexPrices = 0;
		while (priceIterator.hasNext()) {
			assertThat(priceIterator.next().getUnitPrice()).isEqualTo(sortedPrices[indexPrices]);
			indexPrices++;
		}

		storage.sortByPriceReverse();
		Iterator<Product> reversePriceIterator = storage.Iterator();
		Double[] reverseSortedPrices = {200.0, 12.0, 10.0};
		int reverseIndexPrices = 0;
		while (reversePriceIterator.hasNext()) {
			assertThat(reversePriceIterator.next().getUnitPrice()).isEqualTo(reverseSortedPrices[reverseIndexPrices]);
			reverseIndexPrices++;
		}

		storage.sortByStock();
		Iterator<Product> stockIterator = storage.Iterator();
		Long[] sortedStock = {10l, 15l, 20l};
		int indexStock = 0;
		while (stockIterator.hasNext()) {
			assertThat(stockIterator.next().getQuantityInStock()).isEqualTo(sortedStock[indexStock]);
			indexStock++;
		}

		storage.sortByStockReverse();
		Iterator<Product> reverseStockIterator = storage.Iterator();
		Long[] reverseSortedStock = {20l, 15l, 10l};
		int reverseIndexStock = 0;
		while (reverseStockIterator.hasNext()) {
			assertThat(reverseStockIterator.next().getQuantityInStock()).isEqualTo(reverseSortedStock[reverseIndexStock]);
			reverseIndexStock++;
		}

		storage.sortByExpirationDate();
		Iterator<Product> expirationDateIterator = storage.Iterator();
		LocalDate[] sortedExpirationDate = {LocalDate.of(2020, 1, 1), LocalDate.now(), null};
		int indexExpirationDate = 0;
		while (expirationDateIterator.hasNext()) {
			assertThat(expirationDateIterator.next().getExpirationDate()).isEqualTo(sortedExpirationDate[indexExpirationDate]);
			indexExpirationDate++;
		}

		storage.sortByExpirationDateReverse();
		Iterator<Product> reverseExpirationDateIterator = storage.Iterator();
		LocalDate[] reverseSortedExpirationDate = {null, LocalDate.now(),LocalDate.of(2020, 1, 1)};
		int reverseIndexExpirationDate = 0;
		while (reverseExpirationDateIterator.hasNext()) {
			assertThat(reverseExpirationDateIterator.next().getExpirationDate()).isEqualTo(reverseSortedExpirationDate[reverseIndexExpirationDate]);
			reverseIndexExpirationDate++;
		}
    } */

/* 	@Test
	void ShouldSortCombinedLocally() throws Exception{
		Product product = new Product("Apple", "Fruit", 1.5, LocalDate.of(2024,5,1), 100l, LocalDate.now(), LocalDate.now());
        Product product2 = new Product("Apple", "Fruit", 1.5, null, 150l, LocalDate.now(), LocalDate.now());
        Product product3 = new Product("Banana", "Fruit", 0.8, LocalDate.of(2024, 4, 1), 200l, LocalDate.now(), LocalDate.now());
        Product product4 = new Product("Carrot", "Vegetable", 0.5, LocalDate.of(2024, 3, 1), 300l, LocalDate.now(), LocalDate.now());
	    Product product5 = new Product("Banana", "Fruit", 1.0, LocalDate.of(2024, 4, 1), 200l, LocalDate.now(), LocalDate.now());
		Product product6 = new Product("Banana", "Snack", 0.9, LocalDate.of(2024, 4, 1), 100l, LocalDate.now(), LocalDate.now());
		Product product7 = new Product("Apple", "Fruit", 2.0, LocalDate.of(2024, 5, 1), 100l, LocalDate.now(), LocalDate.now());
		Product product8 = new Product("Carrot", "Fruit", 0.5, null, 300l, LocalDate.now(), LocalDate.now());
		Product product9 = new Product("Banana", "Vegetable", 0.8, LocalDate.of(2024, 4, 1), 150l, LocalDate.now(), LocalDate.now());

        ProductStorage storage = new ProductStorage();
        storage.saveProduct(product);
        storage.saveProduct(product2);
        storage.saveProduct(product3);
		storage.saveProduct(product4);
		storage.saveProduct(product5);
		storage.saveProduct(product6);
		storage.saveProduct(product7);
		storage.saveProduct(product8);
		storage.saveProduct(product9);

		storage.sortByNameThenByCategory();
		Iterator<Product> nameThenCategoryIterator = storage.Iterator();
		Product[] sortedNameThenCategory = {product, product2, product7, product3, product5, product6, product9, product8, product4};
		int indexNameCategory = 0;
		while (nameThenCategoryIterator.hasNext()) {
			Product currentProduct = nameThenCategoryIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameThenCategory[indexNameCategory].getName());
			assertThat(currentProduct.getCategory()).isEqualTo(sortedNameThenCategory[indexNameCategory].getCategory());
			indexNameCategory++;
		}

		storage.sortByNameReverseThenByCategory();
		Iterator<Product> nameReverseThenCategoryIterator = storage.Iterator();
		Product[] sortedNameReverseThenCategory = {product8, product4, product3, product5, product6, product9, product, product2, product7};
		int indexNameReverseCategory = 0;
		while (nameReverseThenCategoryIterator.hasNext()) {
			Product currentProduct = nameReverseThenCategoryIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameReverseThenCategory[indexNameReverseCategory].getName());
			assertThat(currentProduct.getCategory()).isEqualTo(sortedNameReverseThenCategory[indexNameReverseCategory].getCategory());
			indexNameReverseCategory++;
		}

		storage.sortByNameThenByCategoryReverse();
		Iterator<Product> nameThenCategoryReverseIterator = storage.Iterator();
		Product[] sortedNameThenCategoryReverse = {product, product2, product7, product9, product6, product3, product5, product4, product8};
		int indexNameCategoryReverse = 0;
		while (nameThenCategoryReverseIterator.hasNext()) {
			Product currentProduct = nameThenCategoryReverseIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameThenCategoryReverse[indexNameCategoryReverse].getName());
			assertThat(currentProduct.getCategory()).isEqualTo(sortedNameThenCategoryReverse[indexNameCategoryReverse].getCategory());
			indexNameCategoryReverse++;
		}

		storage.sortByNameReverseThenByCategoryReverse();
		Iterator<Product> nameReverseThenCategoryReverseIterator = storage.Iterator();
		Product[] sortedNameReverseThenCategoryReverse = {product4, product8, product9, product6, product3, product5, product, product2, product7};
		int indexNameReverseCategoryReverse = 0;
		while (nameReverseThenCategoryReverseIterator.hasNext()) {
			Product currentProduct = nameReverseThenCategoryReverseIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameReverseThenCategoryReverse[indexNameReverseCategoryReverse].getName());
			assertThat(currentProduct.getCategory()).isEqualTo(sortedNameReverseThenCategoryReverse[indexNameReverseCategoryReverse].getCategory());
			indexNameReverseCategoryReverse++;
		}

		storage.sortByNameThenByPrice();
		Iterator<Product> nameThenPriceIterator = storage.Iterator();
		Product[] sortedNameThenPrice = {product, product2, product7, product9, product3, product6, product5, product4, product8};
		int indexNamePrice = 0;
		while (nameThenPriceIterator.hasNext()) {
			Product currentProduct = nameThenPriceIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameThenPrice[indexNamePrice].getName());
			assertThat(currentProduct.getUnitPrice()).isEqualTo(sortedNameThenPrice[indexNamePrice].getUnitPrice());
			indexNamePrice++;
		}

		storage.sortByNameReverseThenByPrice();
		Iterator<Product> nameReverseThenPriceIterator = storage.Iterator();
		Product[] sortedNameReverseThenPrice = {product4, product8, product9, product3, product6, product5, product, product2, product7};
		int indexNameReversePrice = 0;
		while (nameReverseThenPriceIterator.hasNext()) {
			Product currentProduct = nameReverseThenPriceIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameReverseThenPrice[indexNameReversePrice].getName());
			assertThat(currentProduct.getUnitPrice()).isEqualTo(sortedNameReverseThenPrice[indexNameReversePrice].getUnitPrice());
			indexNameReversePrice++;
		}

		storage.sortByNameThenByPriceReverse();
		Iterator<Product> nameThenPriceReverseIterator = storage.Iterator();
		Product[] sortedNameThenPriceReverse = {product7, product, product2, product5, product6, product9, product3, product4, product8};
		int indexNamePriceReverse = 0;
		while (nameThenPriceReverseIterator.hasNext()) {
			Product currentProduct = nameThenPriceReverseIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameThenPriceReverse[indexNamePriceReverse].getName());
			assertThat(currentProduct.getUnitPrice()).isEqualTo(sortedNameThenPriceReverse[indexNamePriceReverse].getUnitPrice());
			indexNamePriceReverse++;
		}

		storage.sortByNameReverseThenByPriceReverse();
		Iterator<Product> nameReverseThenPriceReverseIterator = storage.Iterator();
		Product[] sortedNameReverseThenPriceReverse = {product4, product8, product5, product6, product9, product3, product7, product, product2};
		int indexNameReversePriceReverse = 0;
		while (nameReverseThenPriceReverseIterator.hasNext()) {
			Product currenProduct = nameReverseThenPriceReverseIterator.next();
			assertThat(currenProduct.getName()).isEqualTo(sortedNameReverseThenPriceReverse[indexNameReversePriceReverse].getName());
			assertThat(currenProduct.getUnitPrice()).isEqualTo(sortedNameReverseThenPriceReverse[indexNameReversePriceReverse].getUnitPrice());
			indexNameReversePriceReverse++;
		}

		storage.sortByNameThenStock();
		Iterator<Product> nameThenStockIterator = storage.Iterator();
		Product[] sortedNameThenStock = {product7, product, product2, product6, product9, product5, product3, product4, product8};
		int indexNameStock = 0;
		while (nameThenStockIterator.hasNext()) {
			Product currenProduct = nameThenStockIterator.next();
			assertThat(currenProduct.getName()).isEqualTo(sortedNameThenStock[indexNameStock].getName());
			assertThat(currenProduct.getQuantityInStock()).isEqualTo(sortedNameThenStock[indexNameStock].getQuantityInStock());
			indexNameStock++;
		}

		storage.sortByNameReverseThenStock();
		Iterator<Product> nameReverseThenStockIterator = storage.Iterator();
		Product[] sortedNameReverseThenStock = {product4, product8, product6, product9, product3, product5, product, product7, product2};
		int indexNameReverseStock = 0;
		while (nameReverseThenStockIterator.hasNext()) {
			Product currentProduct = nameReverseThenStockIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameReverseThenStock[indexNameReverseStock].getName());
			assertThat(currentProduct.getQuantityInStock()).isEqualTo(sortedNameReverseThenStock[indexNameReverseStock].getQuantityInStock());
			indexNameReverseStock++;
		}

		storage.sortByNameThenStockReverse();
		Iterator<Product> nameThenStockReverseIterator = storage.Iterator();
		Product[] sortedNameThenStockReverse = {product2, product, product7, product3, product5, product9, product6, product4, product8};
		int indexNameStockReverse = 0;
		while (nameThenStockReverseIterator.hasNext()) {
			Product currentProduct = nameThenStockReverseIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameThenStockReverse[indexNameStockReverse].getName());
			assertThat(currentProduct.getQuantityInStock()).isEqualTo(sortedNameThenStockReverse[indexNameStockReverse].getQuantityInStock());
			indexNameStockReverse++;
		}

		storage.sortByNameReverseThenStockReverse();
		Iterator<Product> nameReverseThenStockReverseIterator = storage.Iterator();
		Product[] sortedNameReverseThenStockReverse = {product4, product8, product3, product5, product9, product6, product2, product, product7};
		int indexNameReverseStockReverse = 0;
		while (nameReverseThenStockReverseIterator.hasNext()) {
			Product currentProduct = nameReverseThenStockReverseIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameReverseThenStockReverse[indexNameReverseStockReverse].getName());
			assertThat(currentProduct.getQuantityInStock()).isEqualTo(sortedNameReverseThenStockReverse[indexNameReverseStockReverse].getQuantityInStock());
			indexNameReverseStockReverse++;
		}

		storage.sortByNameThenExpiration();
		Iterator<Product> nameThenExpirationIterator = storage.Iterator();
		Product[] sortedNameThenExpiration = {product, product7, product2, product3, product5, product6, product9, product4, product8};
		int indexNameExpiration = 0;
		while (nameThenExpirationIterator.hasNext()) {
			Product currentProduct = nameThenExpirationIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameThenExpiration[indexNameExpiration].getName());
			assertThat(currentProduct.getExpirationDate()).isEqualTo(sortedNameThenExpiration[indexNameExpiration].getExpirationDate());
			indexNameExpiration++;
		}

		storage.sortByNameReverseThenExpiration();
		Iterator<Product> nameReverseThenExpirationIterator = storage.Iterator();
		Product[] sortedNameReverseThenExpiration = {product4, product8, product3, product5, product6, product9, product, product7, product2};
		int indexNameReverseExpiration = 0;
		while (nameReverseThenExpirationIterator.hasNext()) {
			Product currentProduct = nameReverseThenExpirationIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameReverseThenExpiration[indexNameReverseExpiration].getName());
			assertThat(currentProduct.getExpirationDate()).isEqualTo(sortedNameReverseThenExpiration[indexNameReverseExpiration].getExpirationDate());
			indexNameReverseExpiration++;
		}

		storage.sortByNameThenExpirationReverse();
		Iterator<Product> nameThenExpirationReverseIterator = storage.Iterator();
		Product[] sortedNameThenExpirationReverse = {product2, product, product7, product3, product5, product6, product9, product8, product4};
		int indexNameExpirationReverse = 0;
		while (nameThenExpirationReverseIterator.hasNext()) {
			Product currentProduct = nameThenExpirationReverseIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameThenExpirationReverse[indexNameExpirationReverse].getName());
			assertThat(currentProduct.getExpirationDate()).isEqualTo(sortedNameThenExpirationReverse[indexNameExpirationReverse].getExpirationDate());
			indexNameExpirationReverse++;
		}

		storage.sortByNameReverseThenExpirationReverse();
		Iterator<Product> nameReverseThenExpirationReverseIterator = storage.Iterator();
		Product[] sortedNameReverseThenExpirationReverse = {product8, product4, product3, product5, product6, product9, product2, product, product7};
		int indexNameReverseExpirationReverse = 0;
		while (nameReverseThenExpirationReverseIterator.hasNext()) {
			Product currentProduct = nameReverseThenExpirationReverseIterator.next();
			assertThat(currentProduct.getName()).isEqualTo(sortedNameReverseThenExpirationReverse[indexNameReverseExpirationReverse].getName());
			assertThat(currentProduct.getExpirationDate()).isEqualTo(sortedNameReverseThenExpirationReverse[indexNameReverseExpirationReverse].getExpirationDate());
			indexNameReverseExpirationReverse++;
		}

		storage.sortByCategoryThenName();
		Iterator<Product> categoryThenNameIterator = storage.Iterator();
		Product[] sortedCategoryThenName = {product, product2, product7, product3, product5, product8, product6, product9, product4};
		int indexCategoryName = 0;
		while (categoryThenNameIterator.hasNext()) {
			Product currentProduct = categoryThenNameIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryThenName[indexCategoryName].getCategory());
			assertThat(currentProduct.getName()).isEqualTo(sortedCategoryThenName[indexCategoryName].getName());
			indexCategoryName++;
		}

		storage.sortByCategoryReverseThenName();
		Iterator<Product> categoryReverseThenNameIterator = storage.Iterator();
		Product[] sortedCategoryReverseThenName = {product9, product4, product6, product, product2, product7, product3, product5, product8};
		int indexCategoryReverseName = 0;
		while (categoryReverseThenNameIterator.hasNext()) {
			Product currentProduct = categoryReverseThenNameIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryReverseThenName[indexCategoryReverseName].getCategory());
			assertThat(currentProduct.getName()).isEqualTo(sortedCategoryReverseThenName[indexCategoryReverseName].getName());
			indexCategoryReverseName++;
		}

		storage.sortByCategoryThenNameReverse();
		Iterator<Product> categoryThenNameReverseIterator = storage.Iterator();
		Product[] sortedCategoryThenNameReverse = {product8, product3, product5, product, product2, product7, product6, product4, product9};
		int indexCategoryNameReverse = 0;
		while (categoryThenNameReverseIterator.hasNext()) {
			Product currentProduct = categoryThenNameReverseIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryThenNameReverse[indexCategoryNameReverse].getCategory());
			assertThat(currentProduct.getName()).isEqualTo(sortedCategoryThenNameReverse[indexCategoryNameReverse].getName());
			indexCategoryNameReverse++;
		}

		storage.sortByCategoryReverseThenNameReverse();
		Iterator<Product> categoryReverseThenNameReverseIterator = storage.Iterator();
		Product[] sortedCategoryReverseThenNameReverse = {product4, product9, product6, product8, product3, product5, product, product2, product7};
		int indexCategoryReverseNameReverse = 0;
		while (categoryReverseThenNameReverseIterator.hasNext()) {
			Product currentProduct = categoryReverseThenNameReverseIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryReverseThenNameReverse[indexCategoryReverseNameReverse].getCategory());
			assertThat(currentProduct.getName()).isEqualTo(sortedCategoryReverseThenNameReverse[indexCategoryReverseNameReverse].getName());
			indexCategoryReverseNameReverse++;
		}

		storage.sortByCategoryThenPrice();
		Iterator<Product> categoryThenPriceIterator = storage.Iterator();
		Product[] sortedCategoryThenPrice = {product8, product3, product5, product, product2, product7, product6, product4, product9};
		int indexCategoryPrice = 0;
		while (categoryThenPriceIterator.hasNext()) {
			Product currentProduct = categoryThenPriceIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryThenPrice[indexCategoryPrice].getCategory());
			assertThat(currentProduct.getUnitPrice()).isEqualTo(sortedCategoryThenPrice[indexCategoryPrice].getUnitPrice());
			indexCategoryPrice++;
		}

		storage.sortByCategoryReverseThenPrice();
		Iterator<Product> categoryReverseThenPriceIterator = storage.Iterator();
		Product[] sortedCategoryReverseThenPrice = {product4, product9, product6, product8, product3, product5, product, product2, product7};
		int indexCategoryReversePrice = 0;
		while (categoryReverseThenPriceIterator.hasNext()) {
			Product currentProduct = categoryReverseThenPriceIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryReverseThenPrice[indexCategoryReversePrice].getCategory());
			assertThat(currentProduct.getUnitPrice()).isEqualTo(sortedCategoryReverseThenPrice[indexCategoryReversePrice].getUnitPrice());
			indexCategoryReversePrice++;
		}

		storage.sortByCategoryReverseThenPriceReverse();
		Iterator<Product> categoryReverseThenPriceReverseIterator = storage.Iterator();
		Product[] sortedCategoryReverseThenPriceReverse = {product9, product4, product6, product7, product, product2, product5, product3, product8};
		int indexCategoryReversePriceReverse = 0;
		while (categoryReverseThenPriceReverseIterator.hasNext()) {
			Product currentProduct = categoryReverseThenPriceReverseIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryReverseThenPriceReverse[indexCategoryReversePriceReverse].getCategory());
			assertThat(currentProduct.getUnitPrice()).isEqualTo(sortedCategoryReverseThenPriceReverse[indexCategoryReversePriceReverse].getUnitPrice());
			indexCategoryReversePriceReverse++;
		}

		storage.sortByCategoryThenStock();
		Iterator<Product> categoryThenStockIterator = storage.Iterator();
		Product[] sortedCategoryThenStock = {product, product7, product2, product3, product5, product8, product6, product9, product4};
		int indexCategoryStock = 0;
		while (categoryThenStockIterator.hasNext()) {
			Product currentProduct = categoryThenStockIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryThenStock[indexCategoryStock].getCategory());
			assertThat(currentProduct.getQuantityInStock()).isEqualTo(sortedCategoryThenStock[indexCategoryStock].getQuantityInStock());
			indexCategoryStock++;
		}

		storage.sortByCategoryReverseThenStock();
		Iterator<Product> categoryReverseThenStockIterator = storage.Iterator();
		Product[] sortedCategoryReverseThenStock = {product9, product4, product6, product, product7, product2, product3, product5, product8};
		int indexCategoryReverseStock = 0;
		while (categoryReverseThenStockIterator.hasNext()) {
			Product currentProduct = categoryReverseThenStockIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryReverseThenStock[indexCategoryReverseStock].getCategory());
			assertThat(currentProduct.getQuantityInStock()).isEqualTo(sortedCategoryReverseThenStock[indexCategoryReverseStock].getQuantityInStock());
			indexCategoryReverseStock++;
		}

		storage.sortByCategoryThenStockReverse();
		Iterator<Product> categoryThenStockReverseIterator = storage.Iterator();
		Product[] sortedCategoryThenStockReverse = {product8, product3, product5, product2, product, product7, product6, product4, product9};
		int indexCategoryStockReverse = 0;
		while (categoryThenStockReverseIterator.hasNext()) {
			Product currentProduct = categoryThenStockReverseIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryThenStockReverse[indexCategoryStockReverse].getCategory());
			assertThat(currentProduct.getQuantityInStock()).isEqualTo(sortedCategoryThenStockReverse[indexCategoryStockReverse].getQuantityInStock());
			indexCategoryStockReverse++;
		}

		storage.sortByCategoryReverseThenStockReverse();
		Iterator<Product> categoryReverseThenStockReverseIterator = storage.Iterator();
		Product[] sortedCategoryReverseThenStockReverse = {product4, product9, product6, product8, product3, product5, product2, product, product7};
		int indexCategoryReverseStockReverse = 0;
		while (categoryReverseThenStockReverseIterator.hasNext()) {
			Product currentProduct = categoryReverseThenStockReverseIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryReverseThenStockReverse[indexCategoryReverseStockReverse].getCategory());
			assertThat(currentProduct.getQuantityInStock()).isEqualTo(sortedCategoryReverseThenStockReverse[indexCategoryReverseStockReverse].getQuantityInStock());
			indexCategoryReverseStockReverse++;
		}

		storage.sortByCategoryThenExpiration();
		Iterator<Product> categoryThenExpirationIterator = storage.Iterator();
		Product[] sortedCategoryThenExpiration = {product3, product5, product, product7, product2, product8, product6, product4, product9};
		int indexCategoryExpiration = 0;
		while (categoryThenExpirationIterator.hasNext()) {
			Product currentProduct = categoryThenExpirationIterator.next();
			assertThat(currentProduct.getCategory()).isEqualTo(sortedCategoryThenExpiration[indexCategoryExpiration].getCategory());
			assertThat(currentProduct.getExpirationDate()).isEqualTo(sortedCategoryThenExpiration[indexCategoryExpiration].getExpirationDate());
			indexCategoryExpiration++;
		}
	} */
}
