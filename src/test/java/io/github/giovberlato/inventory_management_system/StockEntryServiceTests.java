package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.contract.StockEntryAdjustmentDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.StockEntryNotFoundException;
import io.github.giovberlato.inventory_management_system.exception.SupplierNotFoundException;
import io.github.giovberlato.inventory_management_system.exception.WarehouseIsFullException;
import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.model.Supplier;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.model.product.ProductType;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.repository.StockEntryRepository;
import io.github.giovberlato.inventory_management_system.repository.SupplierRepository;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import io.github.giovberlato.inventory_management_system.service.StockEntryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class StockEntryServiceTests {

    @Autowired
    StockEntryRepository stockEntryRepository;

    @Autowired
    StockEntryService stockEntryService;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @BeforeEach
    void setup() {
        stockEntryRepository.deleteAll();
        productRepository.deleteAll();
        warehouseRepository.deleteAll();
        supplierRepository.deleteAll();

        Supplier supplier = new Supplier("MockSupplier", "Alberto Street, 999", "example@example.com", "123456789");
        supplierRepository.save(supplier);
        Product product = new Product("MockProduct", "abcd-1234", ProductType.ELECTRONICS, new BigDecimal("10.89"), 1000, supplier);
        productRepository.save(product);
        Warehouse warehouse = new Warehouse("MockWarehouse", "Example Location, 123", 100000);
        warehouseRepository.save(warehouse);
        StockEntry stockEntry = new StockEntry(product, warehouse, 1000);
        warehouse.setCurrentQuantity(1000); //have to manually set the quantity since we're not using service method calls
        stockEntryRepository.save(stockEntry);
    }

    @Test
    void listAllStocksInWarehouse_ShouldReturnAllStockEntries_IfWarehouseContainsStockEntries() {

        List<StockEntryResponseDTO> testSearchResults = stockEntryService.listAllStocksInWarehouse("mockwarehouse");

        assertFalse(testSearchResults.isEmpty());
        assertTrue(testSearchResults.stream()
                .allMatch(se -> se.getWarehouse()
                        .getName()
                        .equals("MockWarehouse")));
    }

    @Test
    void listAllStocksForProduct_ShouldReturnAllStockEntries_IfProductContainsStockEntries() {
        List<StockEntryResponseDTO> testSearchResults = stockEntryService.listAllStocksForProduct("abcd-1234");

        assertFalse(testSearchResults.isEmpty());
        assertTrue(testSearchResults.stream()
                .allMatch(se -> se.getProduct().getSku().equals("abcd-1234")));
    }

    @Test
    void getStockForProductInWarehouse_ShouldReturnStockEntry_IfEntryExists() {
        StockEntryResponseDTO testSearchResult = stockEntryService.getStockForProductInWarehouse("mockwarehouse", "abcd-1234");

        assertNotNull(testSearchResult);
        assertEquals("MockWarehouse", testSearchResult.getWarehouse().getName());
        assertEquals("abcd-1234", testSearchResult.getProduct().getSku());
    }

    @Test
    void getStockForProductInWarehouse_ShouldThrowException_IfEntryDoesntExist() {
        Product entrylessProduct = new Product(
                "empty product", "defg-1234", ProductType.ELECTRONICS, new BigDecimal("543.23"), 1000,
                supplierRepository.findByNameIgnoreCase("mocksupplier")
                        .orElseThrow(() -> new SupplierNotFoundException("Supplier not found in test.")));

        productRepository.save(entrylessProduct);

        assertThrowsExactly(StockEntryNotFoundException.class,
                () -> stockEntryService.getStockForProductInWarehouse("mockwarehouse", "defg-1234"));
    }

    @Test
    void deleteStockEntry_ShouldDeleteEntry_IfEntryExists() {
        stockEntryService.deleteStockEntry("abcd-1234", "mockwarehouse");

        assertThrowsExactly(StockEntryNotFoundException.class,
                () -> stockEntryService.getStockForProductInWarehouse("mockwarehouse", "abcd-1234"));
    }

    @Test
    void adjustStock_ShouldBeSuccessful_IfValidQuantity() {
        StockEntryAdjustmentDTO adjustment = new StockEntryAdjustmentDTO("abcd-1234", "mockwarehouse", 100);

        StockEntryResponseDTO testAdjustResult = stockEntryService.adjustStock(adjustment);

        assertEquals(1100, testAdjustResult.getQuantity());
        assertEquals(1100, testAdjustResult.getWarehouse().getCurrentQuantity());
    }

    @Test
    void adjustStock_ShouldThrowException_IfWarehouseExceedsMaxCapacity() {

        StockEntryAdjustmentDTO adjustment = new StockEntryAdjustmentDTO("abcd-1234", "mockwarehouse", 9999999);

        assertThrowsExactly(WarehouseIsFullException.class,
                () -> stockEntryService.adjustStock(adjustment));
    }
}

