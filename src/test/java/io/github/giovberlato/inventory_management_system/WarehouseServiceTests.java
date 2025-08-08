package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.exception.WarehouseNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import io.github.giovberlato.inventory_management_system.service.WarehouseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

@Transactional
@SpringBootTest
public class WarehouseServiceTests {

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    WarehouseService warehouseService;

    @BeforeEach
    void setup() {
        warehouseRepository.deleteAll();
        Warehouse warehouse = new Warehouse("MockWarehouse", "Example Location, 123", 1000000);
        warehouseRepository.save(warehouse);
    }

    @Test
    void searchByName_ShouldReturnWarehouse_IfNameExists() {
        String name = "mockwarehouse";

        Warehouse testSearchResult = warehouseService.searchByName(name);

        assertNotNull(testSearchResult);
        assertTrue(testSearchResult.getName().equalsIgnoreCase(name));
    }

    @Test
    void searchByName_ShouldThrowException_IfNameDoesntExist() {
        String name = "nonexistent warehouse";

        assertThrowsExactly(WarehouseNotFoundException.class,
                () -> warehouseService.searchByName(name));
    }

    @Test
    void searchByNameContaining_ShouldReturnWarehouses_IfNameContainsKeyword() {
        String keyword = "Mock";
        List<Warehouse> warehousesFound = warehouseService.searchAllByNameContaining(keyword);

        assertFalse(warehousesFound.isEmpty());
        assertTrue(warehousesFound.stream()
                .allMatch(w -> w.getName().contains(keyword)));
    }

    @Test
    void searchByNameContaining_ShouldThrowException_IfNoWarehouseIsFound() {
        String keyword = "Not a warehouse";

        assertThrowsExactly(WarehouseNotFoundException.class,
                () -> warehouseService.searchAllByNameContaining(keyword));
    }

    @Test
    void searchById_ShouldReturnWarehouse_IfIdExists() {
        warehouseService.addWarehouse(new Warehouse("ExWarehouse", "Example Location, 234", 200000));
        UUID warehouseId = warehouseService.searchByName("exwarehouse").getId();

        Warehouse testSearchResult = warehouseService.searchById(warehouseId);
        assertNotNull(testSearchResult);
        assertEquals(testSearchResult.getId(), warehouseId);
    }

    @Test
    void searchById_ShouldThrowException_IfIdDoesntExist() {
        UUID fakeId = UUID.randomUUID();

        assertThrowsExactly(WarehouseNotFoundException.class,
                () -> warehouseService.searchById(fakeId));
    }

    @Test
    void updateWarehouse_ShouldUpdateWarehouse_IfNameExists() {
        Warehouse updatedWarehouse = new Warehouse("New Warehouse", "New Location, 890", 234567);
        String name = "mockwarehouse";

        Warehouse testUpdateResult = warehouseService.updateWarehouse(updatedWarehouse, name);

        assertEquals(testUpdateResult, updatedWarehouse);
    }

    @Test
    void deleteWarehouse_ShouldDeleteWarehouse_IfNameExists() {
        String name = "mockwarehouse";

        warehouseService.deleteWarehouse(name);

        assertThrowsExactly(WarehouseNotFoundException.class,
                () -> warehouseService.searchAllByNameContaining(name));
    }
}
