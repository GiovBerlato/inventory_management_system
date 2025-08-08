package io.github.giovberlato.inventory_management_system.repository;

import io.github.giovberlato.inventory_management_system.model.Warehouse;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends ListCrudRepository<Warehouse, UUID> {

    Optional<Warehouse> findByNameIgnoreCase(String name);

    Optional<Warehouse> findByLocationIgnoreCase(String location);

    List<Warehouse> findAllByNameContains(String text);
}
