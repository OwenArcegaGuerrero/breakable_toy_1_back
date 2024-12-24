package breakable.toy1.breakable_toy_1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    ProductStorage storage = new ProductStorage();

    @GetMapping("/{id}")
    private ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product requestedProduct = storage.getProductById(id);
        if(requestedProduct == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(requestedProduct);
    }
    
    @PostMapping("/create")
    private ResponseEntity<Void> createProduct(@RequestBody Product Newproduct, UriComponentsBuilder ucb) throws Exception {
        Product savedProduct = storage.saveProduct(Newproduct);
        URI locationOfNewProduct = ucb.path("products/{id}").buildAndExpand(savedProduct.getId()).toUri();

        return ResponseEntity.created(locationOfNewProduct).build();
    }

    @GetMapping
    private ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(storage.getAll());
    }
    
    @PutMapping("/update/{id}")
    private ResponseEntity<Void> updateProductById(@PathVariable Long id, @RequestBody Product updateProduct) throws Exception {
        updateProduct.setId(id);
        try{
            storage.updateProductById(updateProduct, id);
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> deleteProductById(@PathVariable Long id){
        try{
            storage.deleteProduct(id);
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
