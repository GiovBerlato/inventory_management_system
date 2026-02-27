package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.contract.WarehouseRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.WarehouseResponseDTO;
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

        WarehouseResponseDTO testSearchResult = warehouseService.searchByName(name);

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
        List<WarehouseResponseDTO> warehousesFound = warehouseService.searchAllByNameContaining(keyword);

        assertFalse(warehousesFound.isEmpty());
        assertTrue(warehousesFound.stream()
                .allMatch(w -> w.getName().contains(keyword)));
    }

    @Test
    void updateWarehouse_ShouldUpdateWarehouse_IfNameExists() {
        WarehouseRequestDTO updatedWarehouse = new WarehouseRequestDTO("New Warehouse", "New Location, 890", 234567);
        String name = "mockwarehouse";

        WarehouseResponseDTO testUpdateResult = warehouseService.updateWarehouse(updatedWarehouse, name);

        assertEquals(testUpdateResult.getName(), updatedWarehouse.getName());
        assertEquals(testUpdateResult.getLocation(), updatedWarehouse.getLocation());
        assertEquals(testUpdateResult.getMaxCapacity(), updatedWarehouse.getMaxCapacity());
    }

    @Test
    void deleteWarehouse_ShouldDeleteWarehouse_IfNameExists() {
        String name = "mockwarehouse";

        warehouseService.deleteWarehouse(name);

        assertThrowsExactly(WarehouseNotFoundException.class,
                () -> warehouseService.searchByName(name));
    }
}
