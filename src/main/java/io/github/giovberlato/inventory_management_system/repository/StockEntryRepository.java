package io.github.giovberlato.inventory_management_system.repository;

import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockEntryRepository extends ListCrudRepository<StockEntry, UUID> {

    public List<StockEntry> findAllByWarehouse(Warehouse warehouse);

    List<StockEntry> findAllByProduct(Product product);

    public Optional<StockEntry> findByProductAndWarehouse(Product product, Warehouse warehouse);
}
