package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.contract.ProductRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.DuplicateProductException;
import io.github.giovberlato.inventory_management_system.exception.ProductNotFoundException;
import io.github.giovberlato.inventory_management_system.exception.SupplierNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Supplier;
import io.github.giovberlato.inventory_management_system.model.product.*;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.repository.SupplierRepository;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;

    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository, WarehouseRepository warehouseRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<ProductResponseDTO> listAll() {
        return productRepository.findAll().stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public List<ProductResponseDTO> listAllByType(ProductType type) {
        return productRepository.findAllByType(type).stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public List<ProductResponseDTO> searchAllByNameContaining(String keyword) {
        if (keyword != null) {
            return productRepository.findAllByNameContains(keyword).stream()
                    .map(ProductResponseDTO::new)
                    .toList();
        }
        throw new IllegalArgumentException("You must provide text for search.");
    }

    public ProductResponseDTO searchBySku(String sku) {
        if (sku != null) {
            return new ProductResponseDTO(productRepository.findBySkuIgnoreCase(sku)
                    .orElseThrow(() -> new ProductNotFoundException("Product with this SKU was not found.")));
        }
        throw new IllegalArgumentException("You must provide the SKU for search.");
    }

    @Transactional
    public ProductResponseDTO addProduct(ProductRequestDTO request) {
        if (productRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DuplicateProductException("Product with this name already exists.");
        }
        if (productRepository.findBySkuIgnoreCase(request.getSku()).isPresent()) {
            throw new DuplicateProductException("Product with this SKU already exists.");
        }
        Supplier productSupplier = supplierRepository.findByNameContainsIgnoreCase(request.getSupplierName())
                .orElseThrow(() -> new SupplierNotFoundException("Supplier with this name does not exist."));

        Product product = dtoToProduct(request, productSupplier);
        productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    @Transactional
    public void updateProduct(ProductRequestDTO request, String sku) {
        if (!request.getSku().equals(sku)) {
            throw new IllegalArgumentException("SKU parameter does not match the one in the updated Product");
        }
        Product existingProduct = productRepository.findBySkuIgnoreCase(sku)
                .orElseThrow(() -> new ProductNotFoundException("The Product you want to change does not exist."));

        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setType(request.getType());
        existingProduct.setMinimumStock(request.getMinimumStock());

        //only sets supplier if it actually changed
        if (!existingProduct.getSupplier().getName().equalsIgnoreCase(request.getSupplierName())) {
            Supplier newSupplier = supplierRepository.findByNameIgnoreCase(request.getSupplierName())
                    .orElseThrow(() -> new SupplierNotFoundException("Supplier with this name was not found. Create it first."));
            existingProduct.updateSupplier(newSupplier);
        }
    }

    @Transactional
    public void deleteProduct(String sku) {
        Product productToDelete = productRepository.findBySkuIgnoreCase(sku)
                .orElseThrow(() -> new ProductNotFoundException("The product you're trying to delete does not exist"));

        productRepository.delete(productToDelete);
    }


    private Product dtoToProduct(ProductRequestDTO dto, Supplier productSupplier) {
        return new Product(dto.getName(),
                dto.getSku(),
                dto.getType(),
                dto.getPrice(),
                dto.getMinimumStock(),
                productSupplier);
    }
}
