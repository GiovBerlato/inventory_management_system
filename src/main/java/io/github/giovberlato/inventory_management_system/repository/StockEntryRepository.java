package io.github.giovberlato.inventory_management_system.repository;

import io.github.giovberlato.inventory_management_system.model.StockEntry;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockEntryRepository extends ListCrudRepository<StockEntry, UUID> {

    public List<StockEntry> findAllByWarehouse_Id(UUID warehouseId);

    public List<StockEntry> findAllByProduct_Id(UUID productId);

    public Optional<StockEntry> findByWarehouse_IdAndProduct_Id(UUID warehouseId, UUID productId);

    public boolean existsByWarehouse_IdAndProduct_Id(UUID warehouseId, UUID productId);
}
