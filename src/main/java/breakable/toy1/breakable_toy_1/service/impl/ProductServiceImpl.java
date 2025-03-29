package breakable.toy1.breakable_toy_1.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import breakable.toy1.breakable_toy_1.dto.ProductDTO;
import breakable.toy1.breakable_toy_1.dto.ProductCreateDTO;
import breakable.toy1.breakable_toy_1.dto.ProductSearchCriteria;
import breakable.toy1.breakable_toy_1.dto.PaginationRequest;
import breakable.toy1.breakable_toy_1.exception.InvalidProductException;
import breakable.toy1.breakable_toy_1.exception.ProductNotFoundException;
import breakable.toy1.breakable_toy_1.model.PageResponse;
import breakable.toy1.breakable_toy_1.model.Product;
import breakable.toy1.breakable_toy_1.model.SortOrder;
import breakable.toy1.breakable_toy_1.repository.ProductRepository;
import breakable.toy1.breakable_toy_1.service.ProductService;
import breakable.toy1.breakable_toy_1.util.ProductComparator;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public PageResponse<ProductDTO> getAllProducts(ProductSearchCriteria searchCriteria, PaginationRequest pagination) {
        List<Product> allProducts = productRepository.findAll().stream()
                .filter(product -> searchCriteria.getName() == null ||
                        product.getName().contains(searchCriteria.getName()))
                .filter(product -> searchCriteria.getCategory() == null ||
                        searchCriteria.getCategory().isEmpty() ||
                        searchCriteria.getCategory().contains(product.getCategory()))
                .filter(product -> searchCriteria.getAvailability() == null ||
                        "All".equals(searchCriteria.getAvailability()) ||
                        ("In stock".equals(searchCriteria.getAvailability()) && product.inStock()) ||
                        ("Out of stock".equals(searchCriteria.getAvailability()) && !product.inStock()))
                .collect(Collectors.toList());

        // Apply sorting if criteria provided
        if (searchCriteria.getSortBy() != null || searchCriteria.getSecondarySortBy() != null) {
            ProductComparator comparator = new ProductComparator(
                    searchCriteria.getSortBy(),
                    SortOrder.fromString(searchCriteria.getSortOrder()),
                    searchCriteria.getSecondarySortBy(),
                    SortOrder.fromString(searchCriteria.getSecondarySortOrder()));
            allProducts.sort(comparator);
        }

        // Apply pagination
        int start = pagination.getPage() * pagination.getSize();
        List<Product> paginatedProducts;
        if (start >= allProducts.size()) {
            paginatedProducts = new ArrayList<>();
        } else {
            int end = Math.min(start + pagination.getSize(), allProducts.size());
            paginatedProducts = allProducts.subList(start, end);
        }

        // Convert to DTOs
        List<ProductDTO> productDTOs = paginatedProducts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                productDTOs,
                pagination.getPage(),
                pagination.getSize(),
                allProducts.size());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductCreateDTO createDTO) {
        try {
            Product product = new Product(
                    createDTO.getName(),
                    createDTO.getCategory(),
                    createDTO.getUnitPrice(),
                    createDTO.getExpirationDate(),
                    createDTO.getQuantityInStock(),
                    LocalDate.now(),
                    LocalDate.now());
            Product savedProduct = productRepository.save(product);
            return convertToDTO(savedProduct);
        } catch (Exception e) {
            throw new InvalidProductException(e.getMessage());
        }
    }

    @Override
    public void updateProduct(Long id, ProductCreateDTO updateDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        try {
            Product updatedProduct = new Product(
                    updateDTO.getName(),
                    updateDTO.getCategory(),
                    updateDTO.getUnitPrice(),
                    updateDTO.getExpirationDate(),
                    updateDTO.getQuantityInStock(),
                    existingProduct.getCreationDate(),
                    LocalDate.now());
            updatedProduct.setId(id);
            productRepository.save(updatedProduct);
        } catch (Exception e) {
            throw new InvalidProductException(e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.findById(id).isPresent()) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public void markOutOfStock(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setQuantityInStock(0L);
        productRepository.save(product);
    }

    @Override
    public void markInStock(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setQuantityInStock(10L);
        productRepository.save(product);
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .unitPrice(product.getUnitPrice())
                .expirationDate(product.getExpirationDate())
                .quantityInStock(product.getQuantityInStock())
                .creationDate(product.getCreationDate())
                .updateDate(product.getUpdateDate())
                .inStock(product.inStock())
                .build();
    }
}