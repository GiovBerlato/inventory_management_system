package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierResponseDTO;
import io.github.giovberlato.inventory_management_system.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @GetMapping("")
    public SupplierResponseDTO findSupplierByName(@Valid @RequestParam String name) {
        return supplierService.findSupplierByName(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{name}")
    public List<ProductResponseDTO> listAllProductsBySupplier(@Valid @PathVariable String name) {
        return supplierService.listAllProductsBySupplier(name);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void addSupplier(@Valid @RequestBody SupplierRequestDTO supplier) {
        supplierService.addSupplier(supplier);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{name}")
    public void deleteSupplier(@Valid @PathVariable String name) {
        supplierService.deleteSupplier(name);
    }
}
