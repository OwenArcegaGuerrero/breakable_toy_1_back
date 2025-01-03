package breakable.toy1.breakable_toy_1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Array;
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
    private ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ArrayList<String> category,
            @RequestParam(required = false) String availability) {

        List<Product> allProducts = storage.getAll();

        return ResponseEntity
                .ok(allProducts.stream()
                        .filter(product -> name == null || product.getName().contains(name))
                        .filter(product -> category == null || category.isEmpty()
                                || category.contains(product.getCategory()))
                        .filter(product -> availability == null || "All".equals(availability)
                                || ("In stock".equals(availability) && product.inStock())
                                || ("Out of stock".equals(availability) && !product.inStock()))
                        .collect(Collectors.toList()));
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
