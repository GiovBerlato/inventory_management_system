package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.contract.ProductRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.ProductNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Supplier;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.model.product.ProductType;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.repository.SupplierRepository;
import io.github.giovberlato.inventory_management_system.service.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    SupplierRepository supplierRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();

        Supplier supplier = new Supplier("MockSupplier", "Alberto Street, 999", "example@example.com", "123456789");
        supplierRepository.save(supplier);
        Product product = new Product("MockProduct", "abcd-1234", ProductType.ELECTRONICS, new BigDecimal("10.89"), 1000, supplier);
        productRepository.save(product);
    }

    @Test
    void searchBySku_ShouldReturnProduct_IfSkuExists() {
        String sku = "ABCD-1234";

        ProductResponseDTO testSearchResult = productService.searchBySku(sku);

        assertNotNull(testSearchResult);
        assertTrue(testSearchResult.getSku().equalsIgnoreCase(sku));
    }

    @Test
    void searchBySku_ShouldThrowException_IfSkuDoesntExist() {
        String sku = "FAKE-SKU";

        assertThrowsExactly(ProductNotFoundException.class,
                () -> productService.searchBySku(sku));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_IfSkuExists() {
        ProductRequestDTO updatedProduct = new ProductRequestDTO(
                "UpdatedProduct", "abcd-1234", ProductType.ELECTRONICS, BigDecimal.valueOf(500.00), 4567, "MockSupplier");

        productService.updateProduct(updatedProduct, "abcd-1234");

        ProductResponseDTO testUpdateResult = productService.searchBySku("abcd-1234");
        assertEquals(testUpdateResult.getName(), updatedProduct.getName());
        assertEquals(testUpdateResult.getPrice(), updatedProduct.getPrice());
        assertEquals(testUpdateResult.getMinimumStock(), updatedProduct.getMinimumStock());
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_IfSkuExists() {
        String sku = "ABCD-1234";

        productService.deleteProduct(sku);

        assertThrowsExactly(ProductNotFoundException.class,
                () -> productService.searchBySku(sku));
    }
}
