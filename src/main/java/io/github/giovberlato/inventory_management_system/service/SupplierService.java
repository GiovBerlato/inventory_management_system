package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.SupplierNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Supplier;
import io.github.giovberlato.inventory_management_system.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierResponseDTO findSupplierByName(String name) {
        return new SupplierResponseDTO(supplierRepository.findByNameContainsIgnoreCase(name)
                .orElseThrow(() -> new SupplierNotFoundException("No supplier with this name exists.")));
    }

    public List<ProductResponseDTO> listAllProductsBySupplier(String name) {
        Supplier supplier = supplierRepository.findByNameContainsIgnoreCase(name)
                .orElseThrow(() -> new SupplierNotFoundException("No supplier with this name exists."));

        return supplier.getProducts()
                        .stream()
                        .map(ProductResponseDTO::new)
                        .toList();
    }
}
