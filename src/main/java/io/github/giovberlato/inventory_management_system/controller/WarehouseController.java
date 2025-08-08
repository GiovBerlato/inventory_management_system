package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
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
    public List<Warehouse> listAll() {
        return warehouseService.listAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/name-search") // "/name-search?name=warehouse"
    public Warehouse searchByName(@RequestParam String name) {
        return warehouseService.searchByName(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/keyword-search") // "/keyword-search?keyword=ware"
    public List<Warehouse> searchAllByNameContaining(@RequestParam String keyword) {
        return warehouseService.searchAllByNameContaining(keyword);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/id-search") // "/id-search?id=a43b5-342n-12hgs"
    public Warehouse searchById(@RequestParam UUID id) {
        return warehouseService.searchById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Warehouse addWarehouse(@Valid @RequestBody Warehouse warehouse) {
        return warehouseService.addWarehouse(warehouse);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{name}")
    public Warehouse updateWarehouse(@Valid @RequestBody Warehouse warehouse, @PathVariable String name) {
        return warehouseService.updateWarehouse(warehouse, name);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteWarehouse(@Valid @PathVariable String name) {
        warehouseService.deleteWarehouse(name);
    }
}
