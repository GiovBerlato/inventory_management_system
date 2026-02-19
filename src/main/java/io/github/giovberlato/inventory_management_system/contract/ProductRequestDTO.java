package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.product.ProductType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "SKU is required")
    @Pattern(regexp = "[A-Z0-9-]+", message = "SKU must be uppercase alphanumeric")
    private String sku;

    @NotNull(message = "Product Type is required")
    private ProductType type;

    @NotNull
    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer minimumStock;

    @NotBlank
    private String supplierName;
}
