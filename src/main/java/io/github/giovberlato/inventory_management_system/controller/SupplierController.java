package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierResponseDTO;
import io.github.giovberlato.inventory_management_system.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ims/supplier")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Find a supplier by name",
            description = "Returns the supplier details for the exact supplier name provided."
    )
    @GetMapping("")
    public SupplierResponseDTO findSupplierByName(@Valid @RequestParam String name) {
        return supplierService.findSupplierByName(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "List all products from a supplier",
            description = "Shows every product supplied by the selected supplier."
    )
    @GetMapping("/{name}")
    public List<ProductResponseDTO> listAllProductsBySupplier(@Valid @PathVariable String name) {
        return supplierService.listAllProductsBySupplier(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new supplier",
            description = "Registers a new supplier in the system. This action requires manager or administrator permissions."
    )
    @PostMapping("")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void addSupplier(@Valid @RequestBody SupplierRequestDTO supplier) {
        supplierService.addSupplier(supplier);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a supplier",
            description = "Removes the supplier identified by name from the system. This action is restricted to administrators."
    )
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSupplier(@Valid @PathVariable String name) {
        supplierService.deleteSupplier(name);
    }
}
