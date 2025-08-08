package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.exception.DuplicateWarehouseException;
import io.github.giovberlato.inventory_management_system.exception.WarehouseNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseService {
    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    public List<Warehouse> listAll() {
        return repository.findAll();
    }

    public Warehouse searchByName(String name) {
        if (name != null) {
            return repository.findByNameIgnoreCase(name)
                    .orElseThrow(() -> new WarehouseNotFoundException("No warehouse with this name was found."));
        }
        throw new IllegalArgumentException("You must provide a name for search.");
    }

    public List<Warehouse> searchAllByNameContaining(String keyword) {
        if (keyword != null) {
            List<Warehouse> warehousesFound = repository.findAllByNameContains(keyword);
            if (warehousesFound.isEmpty()) {
                throw new WarehouseNotFoundException("No warehouses were found.");
            }
            return warehousesFound;
        }
        throw new IllegalArgumentException("You must provide a keyword for search.");
    }

    public Warehouse searchById(UUID id) {
        if (id != null) {
            return repository.findById(id)
                    .orElseThrow(() -> new WarehouseNotFoundException("No warehouse with this ID was found."));
        }
        throw new IllegalArgumentException("You must provide an ID for search.");
    }

    public Warehouse addWarehouse(Warehouse warehouse) {
        if (repository.findByNameIgnoreCase(warehouse.getName()).isPresent()) {
            throw new DuplicateWarehouseException("There is already a warehouse with this name.");
        }
        if (repository.findByLocationIgnoreCase(warehouse.getLocation()).isPresent()) {
            throw new DuplicateWarehouseException("There is already a warehouse with this address.");
        }
        return repository.save(warehouse);
    }

    public Warehouse updateWarehouse(Warehouse updatedWarehouse, String name) {
        Warehouse warehouseToUpdate = repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new WarehouseNotFoundException("The warehouse you are attempting to update does not exist."));
        warehouseToUpdate.setName(updatedWarehouse.getName());
        warehouseToUpdate.setLocation(updatedWarehouse.getLocation());
        warehouseToUpdate.setMaxCapacity(updatedWarehouse.getMaxCapacity());
        return repository.save(warehouseToUpdate);
    }

    public void deleteWarehouse(String name) {
        Warehouse warehouseToDelete = repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new WarehouseNotFoundException("The warehouse you are attempting to delete does not exist."));
        repository.delete(warehouseToDelete);
    }
}
