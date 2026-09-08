package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.ProductRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.model.product.*;
import io.github.giovberlato.inventory_management_system.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/ims/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "List all products",
            description = "Returns the complete catalog of products currently registered in the inventory system."
    )
    @GetMapping("")
    public List<ProductResponseDTO> listAll() {
        return productService.listAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Filter products by category",
            description = "Returns every product that belongs to the selected category. Available categories are FOOD, ELECTRONICS, CLOTHING, OFFICE_SUPPLIES, HOME_GOODS, TOYS, and OTHER."
    )
    @GetMapping("/filter/{type}")
    public List<ProductResponseDTO> listAllByType(@NotNull @PathVariable ProductType type) {
        return productService.listAllByType(type);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Search products by keyword",
            description = "Returns products whose names contain the provided keyword, helping users find matching items by a partial product name."
    )
    @GetMapping("/keyword-search") // example usage: "/keyword-search?keyword=fork"
    public List<ProductResponseDTO> searchAllByNameContaining(@RequestParam String keyword) {
        return productService.searchAllByNameContaining(keyword);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Find a product by SKU",
            description = "Looks up a single product using its unique SKU code and returns its details."
    )
    @GetMapping("/sku-search") // example usage: "/sku-search?sku=ABC-123"
    public ProductResponseDTO searchProductBySku(@RequestParam String sku) {
        return productService.searchBySku(sku);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new product",
            description = "Registers a new product in the inventory catalog. This action requires manager or administrator permissions."
    )
    @PostMapping("")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ProductResponseDTO addProduct(@Valid @RequestBody ProductRequestDTO product) {
        return productService.addProduct(product);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update an existing product",
            description = "Updates the information of a product identified by its SKU. This action requires manager or administrator permissions."
    )
    @PutMapping("/{sku}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void updateProduct(@Valid @RequestBody ProductRequestDTO product, @PathVariable String sku) {
        productService.updateProduct(product, sku);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a product",
            description = "Removes a product permanently from the catalog using its SKU. This action is restricted to administrators."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{sku}")
    public void deleteProduct(@Valid @PathVariable String sku) {
        productService.deleteProduct(sku);
    }
}
