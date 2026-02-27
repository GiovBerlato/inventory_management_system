package io.github.giovberlato.inventory_management_system.contract;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StockEntryRequestDTO {
    @NotBlank(message = "Must provide SKU for product")
    private String productSKU;
    @NotBlank(message = "Must provide warehouse name")
    private String warehouseName;
    @NotBlank(message = "must provide supplier name")
    private String supplierName;
    @NotNull
    @Min(0)
    private Integer quantity;

    public StockEntryRequestDTO(String productSKU, String warehouseName, String supplierName, Integer quantity) {
        this.productSKU = productSKU;
        this.warehouseName = warehouseName;
        this.supplierName = supplierName;
        this.quantity = quantity;
    }
}
