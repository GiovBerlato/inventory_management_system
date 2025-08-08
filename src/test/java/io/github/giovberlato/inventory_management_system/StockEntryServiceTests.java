package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.contract.StockEntryPostRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryUpdateResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.StockEntryNotFoundException;
import io.github.giovberlato.inventory_management_system.exception.WarehouseIsFullException;
import io.github.giovberlato.inventory_management_system.model.*;
import io.github.giovberlato.inventory_management_system.model.product.*;
import io.github.giovberlato.inventory_management_system.repository.*;
import io.github.giovberlato.inventory_management_system.service.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

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
    WarehouseService warehouseService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        warehouseRepository.deleteAll();

        Product product = new Product("MockProduct", "abcd-1234", ProductType.ELECTRONICS, 1000);
        productRepository.save(product);

        Warehouse warehouse = new Warehouse("MockWarehouse", "Example Location, 123", 1000);
        warehouseRepository.save(warehouse);

        UUID productId = productService.searchBySku("abcd-1234").getId();
        UUID warehouseId = warehouseService.searchByName("MockWarehouse").getId();
        StockEntryPostRequestDTO stockEntry = new StockEntryPostRequestDTO(productId, warehouseId, 100);
        stockEntryService.addStockEntry(stockEntry);
    }

    @Test
    void listAllStocksInWarehouse_ShouldReturnAllStockEntries_IfWarehouseContainsStockEntries() {
        UUID warehouseId = warehouseService.searchByName("mockwarehouse").getId();

        List<StockEntry> testSearchResults = stockEntryService.listAllStocksInWarehouse(warehouseId);

        assertFalse(testSearchResults.isEmpty());
        assertTrue(testSearchResults.stream()
                .allMatch(se -> se.getWarehouse()
                        .getId()
                        .equals(warehouseId)));
    }

    @Test
    void listAllStocksInWarehouse_ShouldThrowException_IfWarehouseIsEmpty() {
        warehouseService.addWarehouse(new Warehouse("Empty warehouse", "ex location 123", 100000));
        UUID emptyWarehouseId = warehouseService.searchByName("empty warehouse").getId();

        assertThrowsExactly(StockEntryNotFoundException.class,
                () -> stockEntryService.listAllStocksInWarehouse(emptyWarehouseId));
    }

    @Test
    void listAllStocksForProduct_ShouldReturnAllStockEntries_IfProductContainsStockEntries() {
        UUID productId = productService.searchBySku("abcd-1234").getId();

        List<StockEntry> testSearchResults = stockEntryService.listAllStocksForProduct(productId);

        assertFalse(testSearchResults.isEmpty());
        assertTrue(testSearchResults.stream()
                .allMatch(se -> se.getProduct()
                        .getId()
                        .equals(productId)));
    }

    @Test
    void listAllStocksForProduct_ShouldThrowException_IfProductIsEmpty() {
        productService.addProduct(new Product("empty product", "defg-1234", ProductType.ELECTRONICS, 1234));
        UUID productId = productService.searchBySku("defg-1234").getId();

        assertThrowsExactly(StockEntryNotFoundException.class,
                () -> stockEntryService.listAllStocksForProduct(productId));
    }

    @Test
    void getStockForProductInWarehouse_ShouldReturnStockEntry_IfEntryExists() {
        UUID productId = productService.searchBySku("abcd-1234").getId();
        UUID warehouseId = warehouseService.searchByName("mockwarehouse").getId();

        StockEntry testSearchResult = stockEntryService.getStockForProductInWarehouse(warehouseId, productId);

        assertNotNull(testSearchResult);
        assertEquals(warehouseId, testSearchResult.getWarehouse().getId());
        assertEquals(productId, testSearchResult.getProduct().getId());
    }

    @Test
    void getStockForProductInWarehouse_ShouldThrowException_IfEntryDoesntExist() {
        Product entrylessProduct = new Product("empty product", "defg-1234", ProductType.ELECTRONICS, 1234);
        productService.addProduct(entrylessProduct);
        UUID productId = productService.searchBySku("defg-1234").getId();
        UUID warehouseId = warehouseService.searchByName("mockwarehouse").getId();

        assertThrowsExactly(StockEntryNotFoundException.class,
                () -> stockEntryService.getStockForProductInWarehouse(warehouseId, productId));
    }

    @Test
    void deleteStockEntry_ShouldDeleteEntry_IfEntryExists() {
        UUID productId = productService.searchBySku("abcd-1234").getId();
        UUID warehouseId = warehouseService.searchByName("mockwarehouse").getId();

        UUID stockEntryId = stockEntryService.getStockForProductInWarehouse(warehouseId, productId).getId();

        stockEntryService.deleteStockEntry(stockEntryId);

        assertThrowsExactly(StockEntryNotFoundException.class,
                () -> stockEntryService.getStockForProductInWarehouse(warehouseId, productId));
    }

    @Test
    void adjustStock_ShouldBeSuccessful_IfValidQuantity() {
        UUID productId = productService.searchBySku("abcd-1234").getId();
        UUID warehouseId = warehouseService.searchByName("mockwarehouse").getId();

        UUID stockEntryId = stockEntryService.getStockForProductInWarehouse(warehouseId, productId).getId();

        StockEntryUpdateResponseDTO testAdjustResult = stockEntryService.adjustStock(stockEntryId, 100);

        assertEquals(200, testAdjustResult.getStockEntry().getQuantity());
    }

    @Test
    void adjustStock_ShouldThrowException_IfWarehouseExceedsMaxCapacity() {
        UUID productId = productService.searchBySku("abcd-1234").getId();
        UUID warehouseId = warehouseService.searchByName("mockwarehouse").getId();

        UUID stockEntryId = stockEntryService.getStockForProductInWarehouse(warehouseId, productId).getId();

        assertThrowsExactly(WarehouseIsFullException.class,
                () -> stockEntryService.adjustStock(stockEntryId, 99999999));
    }
}

