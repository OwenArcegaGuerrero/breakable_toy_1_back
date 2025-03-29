package breakable.toy1.breakable_toy_1.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import breakable.toy1.breakable_toy_1.model.Product;
import breakable.toy1.breakable_toy_1.repository.ProductRepository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private ArrayList<Product> productStorage = new ArrayList<>();

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productStorage);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productStorage.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Invalid Product");
        }

        Optional<Product> existingProduct = findById(product.getId());
        if (existingProduct.isPresent()) {
            int index = productStorage.indexOf(existingProduct.get());
            productStorage.set(index, product);
        } else {
            productStorage.add(product);
        }
        return product;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Product> product = findById(id);
        if (!product.isPresent()) {
            throw new IllegalArgumentException("Invalid id");
        }
        productStorage.remove(product.get());
    }

    @Override
    public void clear() {
        Product.resetidCounter();
        productStorage.clear();
    }
}