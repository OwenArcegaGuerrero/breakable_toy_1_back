package breakable.toy1.breakable_toy_1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import breakable.toy1.breakable_toy_1.dto.ProductDTO;
import breakable.toy1.breakable_toy_1.dto.ProductCreateDTO;
import breakable.toy1.breakable_toy_1.dto.ProductSearchCriteria;
import breakable.toy1.breakable_toy_1.dto.PaginationRequest;
import breakable.toy1.breakable_toy_1.service.ProductService;

import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:8080}")
@Validated
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @Valid ProductSearchCriteria searchCriteria,
            @Valid PaginationRequest pagination) {
        return ResponseEntity.ok(productService.getAllProducts(searchCriteria, pagination));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(
            @Valid @RequestBody ProductCreateDTO product,
            UriComponentsBuilder ucb) {
        ProductDTO savedProduct = productService.createProduct(product);
        URI locationOfNewProduct = ucb
                .path("/api/v1/products/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewProduct).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductCreateDTO product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/outofstock")
    public ResponseEntity<Void> markOutOfStock(@PathVariable Long id) {
        productService.markOutOfStock(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/instock")
    public ResponseEntity<Void> markInStock(@PathVariable Long id) {
        productService.markInStock(id);
        return ResponseEntity.noContent().build();
    }
}