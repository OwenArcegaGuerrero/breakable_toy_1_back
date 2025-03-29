package breakable.toy1.breakable_toy_1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import breakable.toy1.breakable_toy_1.model.Product;

@Repository
public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    void clear();
}