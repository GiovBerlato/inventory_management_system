package io.github.giovberlato.inventory_management_system.contract;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockEntryAdjustmentDTO {
    @NotNull
    private String productSKU;
    @NotNull
    private String warehouseName;
    @NotNull
    private Integer quantityToAdjust;
}
