package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.StockEntryAdjustmentDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryResponseDTO;
import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.service.StockEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ims/stock")
public class StockEntryController {
    private final StockEntryService stockEntryService;

    public StockEntryController(StockEntryService stockEntryService) {
        this.stockEntryService = stockEntryService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/warehouse") // get all stock entries inside a specific warehouse
    public List<StockEntryResponseDTO> listAllStocksInWarehouse(@RequestParam String name) {
        return stockEntryService.listAllStocksInWarehouse(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products") // get all stock entries for a specific product
    public List<StockEntryResponseDTO> listAllStocksForProduct(@RequestParam String sku) {
        return stockEntryService.listAllStocksForProduct(sku);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{warehouseName}/{productSKU}") // get a stock entry for a specific product in a specific warehouse
    public StockEntryResponseDTO getStockForProductInWarehouse(@PathVariable String warehouseName, @PathVariable String productSKU) {
        return stockEntryService.getStockForProductInWarehouse(warehouseName, productSKU);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("") // requires an existing Product and Warehouse
    public void addStockEntry(@Valid @RequestBody StockEntryRequestDTO stockEntryRequest) {
        stockEntryService.addStockEntry(stockEntryRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{warehouseName}/{productSKU}")
    public void deleteStockEntry(@Valid @PathVariable String productSKU, @Valid @PathVariable String warehouseName) {
        stockEntryService.deleteStockEntry(warehouseName, productSKU);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("") // adjust the stock quantity, negative integer to decrease, positive to increase.
    public StockEntryResponseDTO adjustStock(@RequestBody StockEntryAdjustmentDTO request) {
        return stockEntryService.adjustStock(request);
    }
}
