package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StockEntryResponseDTO {
    private String message;
    private ProductResponseDTO product;
    private WarehouseSummaryDTO warehouse;
    private Integer quantity;

    public StockEntryResponseDTO(String message, Product product, Warehouse warehouse, Integer quantity) {
        this.message = message;
        this.product = new ProductResponseDTO(product);
        this.warehouse = new WarehouseSummaryDTO(warehouse);
        this.quantity = quantity;
    }

    public StockEntryResponseDTO(StockEntry stockEntry) {
        this.message = "";
        this.product = new ProductResponseDTO(stockEntry.getProduct());
        this.warehouse = new WarehouseSummaryDTO(stockEntry.getWarehouse());
        this.quantity = stockEntry.getQuantity();
    }
}
