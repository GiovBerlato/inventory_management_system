package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.contract.WarehouseRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.WarehouseResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.DuplicateWarehouseException;
import io.github.giovberlato.inventory_management_system.exception.WarehouseNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseService {
    private final WarehouseRepository repository;

    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
    }

    public List<WarehouseResponseDTO> listAll() {
        return repository.findAll()
                .stream()
                .map(WarehouseResponseDTO::new)
                .toList();
    }

    public WarehouseResponseDTO searchByName(String name) {
        if (!name.isBlank()) {
            return new WarehouseResponseDTO(repository.findByNameIgnoreCase(name)
                    .orElseThrow(() -> new WarehouseNotFoundException("No warehouse with this name was found.")));
        }
        throw new IllegalArgumentException("You must provide a name for search.");
    }

    public List<WarehouseResponseDTO> searchAllByNameContaining(String keyword) {
        if (!keyword.isBlank()) {
            return repository.findAllByNameContains(keyword)
                    .stream()
                    .map(WarehouseResponseDTO::new)
                    .toList();
        }
        throw new IllegalArgumentException("You must provide a keyword for search.");
    }

    @Transactional
    public WarehouseResponseDTO addWarehouse(WarehouseRequestDTO request) {
        if (repository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DuplicateWarehouseException("There is already a warehouse with this name.");
        }
        if (repository.findByLocationIgnoreCase(request.getLocation()).isPresent()) {
            throw new DuplicateWarehouseException("There is already a warehouse with this address.");
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setMaxCapacity(request.getMaxCapacity());
        warehouse.setCurrentQuantity(0);

        Warehouse savedWarehouse = repository.save(warehouse);
        return new WarehouseResponseDTO(savedWarehouse);
    }

    @Transactional
    public WarehouseResponseDTO updateWarehouse(WarehouseRequestDTO updatedWarehouse, String name) {
        Warehouse warehouseToUpdate = repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new WarehouseNotFoundException("The warehouse you are attempting to update does not exist."));

        warehouseToUpdate.setName(updatedWarehouse.getName());
        warehouseToUpdate.setLocation(updatedWarehouse.getLocation());
        warehouseToUpdate.setMaxCapacity(updatedWarehouse.getMaxCapacity());

        Warehouse savedWarehouse = repository.save(warehouseToUpdate);
        return new WarehouseResponseDTO(savedWarehouse);
    }

    @Transactional
    public void deleteWarehouse(String name) {
        Warehouse warehouseToDelete = repository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new WarehouseNotFoundException("The warehouse you are attempting to delete does not exist."));
        repository.delete(warehouseToDelete);
    }
}
