package breakable.toy1.breakable_toy_1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:8080")
public class ProductController {

    @Autowired
    ProductStorage storage = new ProductStorage();

    @GetMapping
    private ResponseEntity<Object> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ArrayList<String> category,
            @RequestParam(required = false) String availability,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String secondarySortBy,
            @RequestParam(required = false) String secondarySortOrder) {

        // Validate and adjust pagination parameters
        page = Math.max(0, page);
        size = Math.max(1, Math.min(50, size)); // Limit page size between 1 and 50

        // Get all products and apply filters
        List<Product> allProducts = storage.getAll().stream()
                .filter(product -> name == null || product.getName().contains(name))
                .filter(product -> category == null || category.isEmpty()
                        || category.contains(product.getCategory()))
                .filter(product -> availability == null || "All".equals(availability)
                        || ("In stock".equals(availability) && product.inStock())
                        || ("Out of stock".equals(availability) && !product.inStock()))
                .collect(Collectors.toList());

        // Apply sorting if parameters are provided
        if (sortBy != null || secondarySortBy != null) {
            ProductComparator comparator = new ProductComparator(
                    sortBy,
                    SortOrder.fromString(sortOrder),
                    secondarySortBy,
                    SortOrder.fromString(secondarySortOrder));
            allProducts.sort(comparator);
        }

        // Return non-paginated response if no pagination parameters are explicitly set
        if (page == 0 && size == 10 && !isPaginationRequested(name, category, availability, sortBy, secondarySortBy)) {
            return ResponseEntity.ok(allProducts);
        }

        // Calculate pagination
        int start = page * size;
        List<Product> paginatedProducts;
        if (start >= allProducts.size()) {
            paginatedProducts = new ArrayList<>();
        } else {
            int end = Math.min(start + size, allProducts.size());
            paginatedProducts = allProducts.subList(start, end);
        }

        // Create pagination response
        PageResponse<Product> response = new PageResponse<>(
                paginatedProducts,
                page,
                size,
                allProducts.size());

        return ResponseEntity.ok(response);
    }

    private boolean isPaginationRequested(String name, ArrayList<String> category, String availability,
            String sortBy, String secondarySortBy) {
        return name != null ||
                (category != null && !category.isEmpty()) ||
                availability != null ||
                sortBy != null ||
                secondarySortBy != null;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product requestedProduct = storage.getProductById(id);
        if (requestedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestedProduct);
    }

    @PostMapping
    private ResponseEntity<Void> createProduct(@RequestBody Product Newproduct, UriComponentsBuilder ucb)
            throws Exception {
        try {
            Product savedProduct = storage.saveProduct(Newproduct);
            URI locationOfNewProduct = ucb.path("products/{id}").buildAndExpand(savedProduct.getId()).toUri();
            return ResponseEntity.created(locationOfNewProduct).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/{id}/outofstock")
    public ResponseEntity<Void> outOfStock(@PathVariable Long id) throws Exception {
        try {
            storage.outOfStock(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/instock")
    public ResponseEntity<Void> inStock(@PathVariable Long id) {
        try {
            storage.inStock(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Void> updateProductById(@PathVariable Long id, @RequestBody Product updateProduct)
            throws Exception {
        updateProduct.setId(id);
        try {
            storage.updateProductById(updateProduct, id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        try {
            storage.deleteProduct(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
