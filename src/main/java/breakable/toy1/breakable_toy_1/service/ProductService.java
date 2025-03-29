package breakable.toy1.breakable_toy_1.service;

import breakable.toy1.breakable_toy_1.dto.ProductDTO;
import breakable.toy1.breakable_toy_1.dto.ProductCreateDTO;
import breakable.toy1.breakable_toy_1.dto.ProductSearchCriteria;
import breakable.toy1.breakable_toy_1.dto.PaginationRequest;
import breakable.toy1.breakable_toy_1.model.PageResponse;

public interface ProductService {
    PageResponse<ProductDTO> getAllProducts(ProductSearchCriteria searchCriteria, PaginationRequest pagination);

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductCreateDTO product);

    ProductDTO updateProduct(Long id, ProductCreateDTO product);

    void deleteProduct(Long id);

    void markOutOfStock(Long id);

    void markInStock(Long id);
}