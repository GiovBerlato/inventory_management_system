package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.ProductRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.model.product.*;
import io.github.giovberlato.inventory_management_system.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
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
    @GetMapping("")
    public List<ProductResponseDTO> listAll() {
        return productService.listAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter/{type}")
    public List<ProductResponseDTO> listAllByType(@NotNull @PathVariable ProductType type) {
        return productService.listAllByType(type);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/keyword-search") // example usage: "/keyword-search?keyword=fork"
    public List<ProductResponseDTO> searchAllByNameContaining(@RequestParam String keyword) {
        return productService.searchAllByNameContaining(keyword);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sku-search") // example usage: "/sku-search?sku=ABC-123"
    public ProductResponseDTO searchProductBySku(@RequestParam String sku) {
        return productService.searchBySku(sku);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ProductResponseDTO addProduct(@Valid @RequestBody ProductRequestDTO product) {
        return productService.addProduct(product);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{sku}")
    public void updateProduct(@Valid @RequestBody ProductRequestDTO product, @PathVariable String sku) {
        productService.updateProduct(product, sku);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{sku}")
    public void deleteProduct(@Valid @PathVariable String sku) {
        productService.deleteProduct(sku);
    }
}
