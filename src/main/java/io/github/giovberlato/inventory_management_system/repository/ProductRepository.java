package io.github.giovberlato.inventory_management_system.repository;

import io.github.giovberlato.inventory_management_system.model.product.Product;
import lombok.NonNull;
import org.springframework.data.repository.ListCrudRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends ListCrudRepository<Product, UUID> {

    Optional<Product> findByNameIgnoreCase(String name);

    Optional<Product> findBySkuIgnoreCase(String sku);

    List<Product> findAllByType(Type type);

    List<Product> findAllByNameContains(String keyword);

}
