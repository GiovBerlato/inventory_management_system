package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.DuplicateSupplierException;
import io.github.giovberlato.inventory_management_system.exception.SupplierNotFoundException;
import io.github.giovberlato.inventory_management_system.model.Supplier;
import io.github.giovberlato.inventory_management_system.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierResponseDTO findSupplierByName(String name) {
        return new SupplierResponseDTO(supplierRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new SupplierNotFoundException("No supplier with this name exists.")));
    }

    public List<ProductResponseDTO> listAllProductsBySupplier(String name) {
        Supplier supplier = supplierRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new SupplierNotFoundException("No supplier with this name exists."));

        return supplier.getProducts()
                        .stream()
                        .map(ProductResponseDTO::new)
                        .toList();
    }

    @Transactional
    public SupplierResponseDTO addSupplier(SupplierRequestDTO request) {
        if (supplierRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DuplicateSupplierException("A supplier with this name already exists.");
        }
        Supplier savedSupplier = supplierRepository.save(dtoToSupplier(request));
        return new SupplierResponseDTO(savedSupplier);
    }

    @Transactional
    public void deleteSupplier(String name) {
        Supplier supplier = supplierRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new SupplierNotFoundException("No supplier with this name exists."));
        supplierRepository.delete(supplier);
    }

    private Supplier dtoToSupplier(SupplierRequestDTO dto) {
        return new Supplier(dto.getName(),
                dto.getAddress(),
                dto.getContactNumber(),
                dto.getEmail());
    }
}
