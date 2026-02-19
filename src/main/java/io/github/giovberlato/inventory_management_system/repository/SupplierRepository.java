package io.github.giovberlato.inventory_management_system.repository;

import io.github.giovberlato.inventory_management_system.model.Supplier;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends ListCrudRepository<Supplier, UUID> {

    Optional<Supplier> findByNameContainsIgnoreCase(String name);

    Optional<Supplier> findByContactNumber(String number);

    Optional<Supplier> findByAddressContainsIgnoreCase(String address);

    Optional<Supplier> findByNameIgnoreCase(String name);
}
