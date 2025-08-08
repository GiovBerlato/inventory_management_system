package io.github.giovberlato.inventory_management_system.service;

import io.github.giovberlato.inventory_management_system.contract.StockEntryPostRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryUpdateResponseDTO;
import io.github.giovberlato.inventory_management_system.exception.*;
import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.repository.ProductRepository;
import io.github.giovberlato.inventory_management_system.repository.StockEntryRepository;
import io.github.giovberlato.inventory_management_system.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

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

    public List<StockEntry> listAllStocksInWarehouse(UUID warehouseId) {
        List<StockEntry> warehouseStock = stockEntryRepository.findAllByWarehouse_Id(warehouseId);
        if (warehouseStock.isEmpty()) {
            throw new StockEntryNotFoundException("This warehouse currently has no items registered in stock.");
        }
        return warehouseStock;
    }

    public List<StockEntry> listAllStocksForProduct(UUID productId) {
        List<StockEntry> warehouseStock = stockEntryRepository.findAllByProduct_Id(productId);
        if (warehouseStock.isEmpty()) {
            throw new StockEntryNotFoundException("This warehouse currently has no items registered in stock.");
        }
        return warehouseStock;
    }

    public StockEntry getStockForProductInWarehouse(UUID warehouseId, UUID productId) {
        return stockEntryRepository.findByWarehouse_IdAndProduct_Id(warehouseId, productId)
                .orElseThrow(() -> new StockEntryNotFoundException("The stock entry you're attempting to search does not exist, check your warehouse and product IDs."));
    }

    public StockEntry addStockEntry(StockEntryPostRequestDTO stockEntryRequest) {
        Product product = productRepository.findById(stockEntryRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("No product with this ID was found."));
        Warehouse warehouse = warehouseRepository.findById(stockEntryRequest.getWarehouseId())
                .orElseThrow(() -> new WarehouseNotFoundException("No warehouse with this ID was found."));
        if (stockEntryRepository.findByWarehouse_IdAndProduct_Id(warehouse.getId(), product.getId()).isPresent()) {
            throw new DuplicateStockEntryException("A stock entry for this product in this warehouse already exists.");
        }
        StockEntry newStockEntry = new StockEntry();
        newStockEntry.setProduct(product);
        newStockEntry.setWarehouse(warehouse);
        Integer newStockEntryQty = stockEntryRequest.getQuantity();
        validateAndUpdateWarehouseQuantity(newStockEntry, newStockEntryQty);
        newStockEntry.setQuantity(newStockEntryQty);

        return stockEntryRepository.save(newStockEntry);
    }

    public void deleteStockEntry(UUID id) {
        StockEntry stockEntryToDelete = stockEntryRepository.findById(id)
                .orElseThrow(() -> new StockEntryNotFoundException("The stock entry you're trying to delete does not exist."));
        stockEntryRepository.delete(stockEntryToDelete);
    }

    public StockEntryUpdateResponseDTO adjustStock(UUID id, Integer quantityToAdjust) {
        StockEntry stockEntry = stockEntryRepository.findById(id)
                .orElseThrow(() -> new StockEntryNotFoundException("The stock entry you're trying to update does not exist."));
        StockEntryUpdateResponseDTO response = new StockEntryUpdateResponseDTO("Update Successful", stockEntry); // defaults to normal success message

        Integer productMinimumStock = stockEntry.getProduct().getMinimumStock();
        Integer stockBeforeAdjustment = stockEntry.getQuantity();
        Integer updatedStock = stockBeforeAdjustment + quantityToAdjust;
        if (updatedStock < 0) {
            stockEntry.setQuantity(0);
        } else {
            stockEntry.setQuantity(updatedStock);
        }
        validateAndUpdateWarehouseQuantity(stockEntry, stockEntry.getQuantity() - stockBeforeAdjustment);
        if (stockEntry.getQuantity() < productMinimumStock) {
            // send warning regarding stockEntry's quantity being below the minimum stock set for the product.
            response.setMessage("Update Successful. (Warning: Stock for this entry is currently below the minimum stock set for the Product.)");
        }
        stockEntryRepository.save(stockEntry);
        return response;
    }

    private void validateAndUpdateWarehouseQuantity(StockEntry stockEntry, Integer quantityToAdjust) {
        Integer warehouseCurrentQuantity = stockEntry.getWarehouse().getCurrentQuantity();
        Integer warehouseMaxCapacity = stockEntry.getWarehouse().getMaxCapacity();
        Integer updatedQuantity = warehouseCurrentQuantity + quantityToAdjust;
        if (updatedQuantity > warehouseMaxCapacity) {
            throw new WarehouseIsFullException("You tried to add " + quantityToAdjust + " units of a product, but this warehouse " +
                    "only has space for " + (warehouseMaxCapacity - warehouseCurrentQuantity) + " units.");
        } else if (updatedQuantity < 0) {
            throw new IllegalArgumentException("You tried to take " + quantityToAdjust + " units of a product, but this warehouse " +
                    "only has " + warehouseCurrentQuantity + " units.");
        }
        stockEntry.getWarehouse().setCurrentQuantity(warehouseCurrentQuantity + quantityToAdjust);
    }
}
