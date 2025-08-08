package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.model.product.Product;

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
    public List<Product> listAll() {
        return productService.listAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/filter/{type}")
    public List<Product> listAllByType(@NotNull @PathVariable Type type) {
        return productService.listAllByType(type);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/keyword-search") // example usage: "/keyword-search?keyword=fork"
    public List<Product> searchAllByNameContaining(@RequestParam String keyword) {
        return productService.searchAllByNameContaining(keyword);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sku-search") // example usage: "/sku-search?sku=ABC-123"
    public Product searchProductBySku(@RequestParam String sku) {
        return productService.searchBySku(sku);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Product addProduct(@Valid @RequestBody Product product) {
        return productService.addProduct(product);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{sku}")
    public Product updateProduct(@Valid @RequestBody Product product, @PathVariable String sku) {
        return productService.updateProduct(product, sku);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{sku}")
    public void deleteProduct(@Valid @PathVariable String sku) {
        productService.deleteProduct(sku);
    }
}
