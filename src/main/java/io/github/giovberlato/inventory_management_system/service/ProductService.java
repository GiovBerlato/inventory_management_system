package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.exception.DuplicateProductException;
import io.github.giovberlato.inventory_management_system.exception.ProductNotFoundException;
import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public ProductService(ProductRepository productRepository, WarehouseRepository warehouseRepository) {
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public List<Product> listAllByType(Type type) {
        List<Product> productsByType = productRepository.findAllByType(type);
        if (productsByType.isEmpty()) {
            throw new ProductNotFoundException("No Products of this type were found.");
        }
        return productsByType;
    }

    public List<Product> searchAllByNameContaining(String keyword) {
        if (keyword != null) {
            List<Product> productsFound = productRepository.findAllByNameContains(keyword);
            if (productsFound.isEmpty()) {
                throw new ProductNotFoundException("Product containing this name was not found.");
            }
            return productsFound;
        }
        throw new IllegalArgumentException("You must provide text for search.");
    }

    public Product searchBySku(String sku) {
        if (sku != null) {
            return productRepository.findBySkuIgnoreCase(sku)
                    .orElseThrow(() -> new ProductNotFoundException("Product with this SKU was not found."));
        }
        throw new IllegalArgumentException("You must provide the SKU for search.");
    }

    public Product addProduct(Product product) {
        if (productRepository.findByNameIgnoreCase(product.getName()).isPresent()) {
            throw new DuplicateProductException("Product with this name already exists.");
        }
        if (productRepository.findBySkuIgnoreCase(product.getSku()).isPresent()) {
            throw new DuplicateProductException("Product with this SKU already exists.");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, String sku) {
        if (!product.getSku().equalsIgnoreCase(sku)) {
            throw new IllegalArgumentException("SKU parameter does not match the one in the updated Product");
        }
        productRepository.findBySkuIgnoreCase(sku).ifPresentOrElse(productRepository::delete,
                () -> {
            throw new ProductNotFoundException("The Product you want to change does not exist, please create it first.");
        });
        return productRepository.save(product);
    }

    public void deleteProduct(String sku) {
        Product productToDelete = productRepository.findBySkuIgnoreCase(sku)
                .orElseThrow(() -> new ProductNotFoundException("The product you're trying to delete does not exist"));

        productRepository.delete(productToDelete);
    }
}
