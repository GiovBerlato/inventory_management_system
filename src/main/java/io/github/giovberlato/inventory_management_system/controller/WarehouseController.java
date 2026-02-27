package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.WarehouseRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.WarehouseResponseDTO;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @GetMapping("")
    public List<WarehouseResponseDTO> listAll() {
        return warehouseService.listAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter") // "/ims/warehouses/filter?name=warehouse"
    public WarehouseResponseDTO searchByName(@RequestParam String name) {
        return warehouseService.searchByName(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/keyword-search") // "/keyword-search?keyword=ware"
    public List<WarehouseResponseDTO> searchAllByNameContaining(@RequestParam String keyword) {
        return warehouseService.searchAllByNameContaining(keyword);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public WarehouseResponseDTO addWarehouse(@Valid @RequestBody WarehouseRequestDTO warehouse) {
        return warehouseService.addWarehouse(warehouse);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{name}")
    public WarehouseResponseDTO updateWarehouse(@Valid @RequestBody WarehouseRequestDTO warehouse, @PathVariable String name) {
        return warehouseService.updateWarehouse(warehouse, name);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteWarehouse(@Valid @PathVariable String name) {
        warehouseService.deleteWarehouse(name);
    }
}
