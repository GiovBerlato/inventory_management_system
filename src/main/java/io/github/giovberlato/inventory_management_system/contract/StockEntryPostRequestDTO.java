package io.github.giovberlato.inventory_management_system.contract;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

// DTO for POSTing a StockEntry
@Getter
@Setter
public class StockEntryPostRequestDTO {
    @NotNull
    private UUID productId;
    @NotNull
    private UUID warehouseId;
    @NotNull
    @Min(0)
    private Integer quantity;

    // constructor for testing
    public StockEntryPostRequestDTO(UUID productId, UUID warehouseId, Integer quantity) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }
}
