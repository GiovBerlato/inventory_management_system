package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.SupplierNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Supplier;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.model.product.ProductType;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.repository.SupplierRepository;
import io.github.giovberlato.inventory_management_system.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class SupplierServiceTests {

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplierService supplierService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();

        Supplier supplier = new Supplier("MockSupplier", "Alberto Street, 999", "example@example.com", "123456789");
        Product product = new Product("MockProduct", "abcd-1234", ProductType.ELECTRONICS, new BigDecimal("10.89"), 1000, supplier);
        supplier.getProducts().add(product);
        supplierRepository.save(supplier);
        productRepository.save(product);
    }

    @Test
    void findSupplierByName_ShouldReturnSupplier_IfNameExists() {
        String name = "mocksupplier";

        SupplierResponseDTO response = supplierService.findSupplierByName(name);
        assertNotNull(response);
        assertTrue(response.getName().equalsIgnoreCase(name));
    }

    @Test
    void findSupplierByName_ShouldThrowException_IfNameDoesntExist() {
        assertThrowsExactly(SupplierNotFoundException.class,
                () -> supplierService.findSupplierByName("nonexistent supplier"));
    }

    @Test
    void listAllProductsBySupplier_ShouldReturnAllProducts_IfSupplierContainsProducts() {
        String name = "mocksupplier";
        List<ProductResponseDTO> testSearchResults = supplierService.listAllProductsBySupplier(name);

        assertFalse(testSearchResults.isEmpty());
        assertTrue(testSearchResults.stream()
                .allMatch(p -> p.getSupplier().getName().equalsIgnoreCase(name)));
    }

    @Test
    void listAllProductsBySupplier_ShouldThrowException_IfNameDoesntExist() {
        assertThrowsExactly(SupplierNotFoundException.class,
                () -> supplierService.listAllProductsBySupplier("nonexistent supplier"));
    }

    @Test
    void deleteSupplier_ShouldDeleteSupplier_IfNameExists() {
        String name = "mocksupplier";

        supplierService.deleteSupplier(name);

        assertThrowsExactly(SupplierNotFoundException.class,
                () -> supplierService.findSupplierByName(name));
    }
}
