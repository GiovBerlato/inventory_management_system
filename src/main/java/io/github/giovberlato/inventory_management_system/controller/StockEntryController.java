package io.github.giovberlato.inventory_management_system.controller;

import io.github.giovberlato.inventory_management_system.contract.StockEntryPostRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryUpdateResponseDTO;
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
    public List<StockEntry> listAllStocksInWarehouse(@RequestParam UUID id) {
        return stockEntryService.listAllStocksInWarehouse(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products") // get all stock entries for a specific product
    public List<StockEntry> listAllStocksForProduct(@RequestParam UUID id) {
        return stockEntryService.listAllStocksForProduct(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{warehouseId}/{productId}") // get a stock entry for a specific product in a specific warehouse
    public StockEntry getStockForProductInWarehouse(@PathVariable UUID warehouseId, @PathVariable UUID productId) {
        return stockEntryService.getStockForProductInWarehouse(warehouseId, productId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("") // requires an existing Product and Warehouse
    public StockEntry addStockEntry(@Valid @RequestBody StockEntryPostRequestDTO stockEntryRequest) {
        return stockEntryService.addStockEntry(stockEntryRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{stockEntryId}")
    public void deleteStockEntry(@Valid @RequestBody UUID id) {
        stockEntryService.deleteStockEntry(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{stockEntryId}") // adjust the stock quantity, negative integer to decrease, positive to increase.
    public StockEntryUpdateResponseDTO adjustStock(@Valid @PathVariable UUID stockEntryId, @Valid @RequestParam Integer quantity) {
        return stockEntryService.adjustStock(stockEntryId, quantity);
    }
}
