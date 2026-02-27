package io.github.giovberlato.inventory_management_system.repository;

import io.github.giovberlato.inventory_management_system.model.product.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends ListCrudRepository<Product, UUID> {

    Optional<Product> findByNameIgnoreCase(String name);

    Optional<Product> findBySkuIgnoreCase(String sku);

    @EntityGraph(attributePaths = {"supplier"})
    List<Product> findAll();

    @EntityGraph(attributePaths = {"supplier"})
    List<Product> findAllByType(ProductType type);

    @EntityGraph(attributePaths = {"supplier"})
    List<Product> findAllByNameContains(String keyword);

}
