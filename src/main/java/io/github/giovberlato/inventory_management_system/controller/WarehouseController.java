package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.WarehouseRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.WarehouseResponseDTO;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ims/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "List all warehouses",
            description = "Returns every warehouse currently registered in the system."
    )
    @GetMapping("")
    public List<WarehouseResponseDTO> listAll() {
        return warehouseService.listAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Find a warehouse by name",
            description = "Returns the warehouse that matches the exact name provided."
    )
    @GetMapping("/filter") // "/ims/warehouses/filter?name=warehouse"
    public WarehouseResponseDTO searchByName(@RequestParam String name) {
        return warehouseService.searchByName(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Search warehouses by keyword",
            description = "Returns warehouses whose names contain the given keyword, making it easier to find similar warehouse names."
    )
    @GetMapping("/keyword-search") // "/keyword-search?keyword=ware"
    public List<WarehouseResponseDTO> searchAllByNameContaining(@RequestParam String keyword) {
        return warehouseService.searchAllByNameContaining(keyword);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a warehouse",
            description = "Registers a new warehouse in the inventory system. This action requires manager or administrator permissions."
    )
    @PostMapping("")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public WarehouseResponseDTO addWarehouse(@Valid @RequestBody WarehouseRequestDTO warehouse) {
        return warehouseService.addWarehouse(warehouse);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update an existing warehouse",
            description = "Updates the details of a warehouse identified by its name. This action requires manager or administrator permissions."
    )
    @PutMapping("/{name}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public WarehouseResponseDTO updateWarehouse(@Valid @RequestBody WarehouseRequestDTO warehouse, @PathVariable String name) {
        return warehouseService.updateWarehouse(warehouse, name);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a warehouse",
            description = "Removes the warehouse identified by name from the system. This action is restricted to administrators."
    )
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWarehouse(@Valid @PathVariable String name) {
        warehouseService.deleteWarehouse(name);
    }
}
