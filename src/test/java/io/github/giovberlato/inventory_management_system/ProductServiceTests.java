package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.exception.ProductNotFoundException;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.model.product.ProductType;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.service.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class ProductServiceTests {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        Product product = new Product("MockProduct", "abcd-1234", ProductType.ELECTRONICS, 1000);
        productRepository.save(product);
    }

    @Test
    void searchBySku_ShouldReturnProduct_IfSkuExists() {
        String sku = "ABCD-1234";

        Product testSearchResult = productService.searchBySku(sku);

        assertNotNull(testSearchResult);
        assertTrue(sku.equalsIgnoreCase(testSearchResult.getSku()));
    }

    @Test
    void searchBySku_ShouldThrowException_IfSkuDoesntExist() {
        String sku = "FAKE-SKU";

        assertThrowsExactly(ProductNotFoundException.class,
                () -> productService.searchBySku(sku));
    }

    @Test
    void updateProduct_ShouldUpdateProduct_IfSkuExists() {
        Product updatedProduct = new Product("UpdatedProduct", "abcd-1234", ProductType.ELECTRONICS, 500);

        Product testUpdateResult = productService.updateProduct(updatedProduct, "abcd-1234");

        assertEquals(testUpdateResult, updatedProduct);
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_IfSkuExists() {
        String sku = "ABCD-1234";

        productService.deleteProduct(sku);

        assertThrowsExactly(ProductNotFoundException.class,
                () -> productService.searchBySku(sku));
    }
}
