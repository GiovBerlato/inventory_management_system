package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.StockEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// sends a warning message if stock quantity goes below minimum stock for the product.
public class StockEntryUpdateResponseDTO {
    private String message;
    private StockEntry stockEntry;
}
