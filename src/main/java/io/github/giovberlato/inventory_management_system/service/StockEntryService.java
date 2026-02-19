package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.contract.StockEntryAdjustmentDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.*;
import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.repository.StockEntryRepository;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class StockEntryService {
    private final StockEntryRepository stockEntryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    public StockEntryService(StockEntryRepository stockEntryRepository, ProductRepository productRepository, WarehouseRepository warehouseRepository) {
        this.stockEntryRepository = stockEntryRepository;
        this.productRepository = productRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<StockEntryResponseDTO> listAllStocksInWarehouse(String warehouseName) {
        Warehouse warehouse = warehouseRepository.findByNameIgnoreCase(warehouseName)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse with this name not found."));
        List<StockEntry> warehouseStock = stockEntryRepository.findAllByWarehouse(warehouse);
        return warehouseStock.stream()
                .map(StockEntryResponseDTO::new)
                .toList();
    }

    public List<StockEntryResponseDTO> listAllStocksForProduct(String productSKU) {
        Product product = productRepository.findBySkuIgnoreCase(productSKU)
                .orElseThrow(() -> new ProductNotFoundException("Product with this SKU was not found."));
        List<StockEntry> productEntries = stockEntryRepository.findAllByProduct(product);
        return productEntries.stream()
                .map(StockEntryResponseDTO::new)
                .toList();
    }

    public StockEntryResponseDTO getStockForProductInWarehouse(String warehouseName, String productSKU) {
        Product product = productRepository.findBySkuIgnoreCase(productSKU)
                .orElseThrow(() -> new ProductNotFoundException("Product with this SKU was not found."));
        Warehouse warehouse = warehouseRepository.findByNameIgnoreCase(warehouseName)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse with this name not found."));
        StockEntry stockEntry = stockEntryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseThrow(() -> new StockEntryNotFoundException("The stock entry you're attempting to search does not appear to exist, check your warehouse and product info."));

        return new StockEntryResponseDTO(stockEntry);
    }

    @Transactional
    public void addStockEntry(StockEntryRequestDTO request) {

        Product product = productRepository.findBySkuIgnoreCase(request.getProductSKU())
                .orElseThrow(() -> new ProductNotFoundException("Product with this SKU was not found."));
        Warehouse warehouse = warehouseRepository.findByNameIgnoreCase(request.getWarehouseName())
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse with this name not found."));
        if (stockEntryRepository.findByProductAndWarehouse(product, warehouse).isPresent()) {
            throw new DuplicateStockEntryException("A stock entry for this product already exists in this warehouse.");
        }
        StockEntry newEntry = new StockEntry();
        newEntry.setProduct(product);
        newEntry.setWarehouse(warehouse);
        Integer newStockEntryQty = request.getQuantity();
        newEntry.setQuantity(newStockEntryQty);

        stockEntryRepository.save(newEntry);
    }

    @Transactional
    public void deleteStockEntry(String productSKU, String warehouseName) {
        Product product = productRepository.findBySkuIgnoreCase(productSKU)
                .orElseThrow(() -> new ProductNotFoundException("Product with this SKU was not found."));
        Warehouse warehouse = warehouseRepository.findByNameIgnoreCase(warehouseName)
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse with this name not found."));
        StockEntry stockEntryToDelete = stockEntryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseThrow(() -> new StockEntryNotFoundException("The stock entry you're trying to delete does not exist."));
        stockEntryRepository.delete(stockEntryToDelete);
    }

    @Transactional
    public StockEntryResponseDTO adjustStock(StockEntryAdjustmentDTO request) {
        Product product = productRepository.findBySkuIgnoreCase(request.getProductSKU())
                .orElseThrow(() -> new ProductNotFoundException("Product with this SKU was not found."));
        Warehouse warehouse = warehouseRepository.findByNameIgnoreCase(request.getWarehouseName())
                .orElseThrow(() -> new WarehouseNotFoundException("Warehouse with this name not found."));
        StockEntry stockEntry = stockEntryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseThrow(() -> new StockEntryNotFoundException("You must create a stock entry for this product on this warehouse before adjusting stock."));

        int currentStock = stockEntry.getQuantity();
        int qtyToAdjust = request.getQuantityToAdjust();
        int updatedStock = currentStock + qtyToAdjust;

        if (updatedStock < 0) {
            throw new InsufficientStockException(
                    "Cannot remove " + Math.abs(qtyToAdjust) + "items, this stock entry currently only has " + currentStock + " available.");
        }
        int warehouseCurrentQuantity = warehouse.getCurrentQuantity();
        int warehouseNewQuantity = warehouseCurrentQuantity + qtyToAdjust;

        if (warehouseNewQuantity > warehouse.getMaxCapacity()) {
            throw new WarehouseIsFullException("Warehouse capacity exceeded.");
        }

        stockEntry.setQuantity(updatedStock);
        warehouse.setCurrentQuantity(warehouseNewQuantity);

        String message = "Update Successful.";
        if (updatedStock < product.getMinimumStock()) {
            message = "Update Successful. (WARNING: Stock below specified minimum threshold.)";
        }

        return new StockEntryResponseDTO(message, product, warehouse);
    }
}
