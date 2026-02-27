package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.product.Product;
import io.github.giovberlato.inventory_management_system.model.product.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponseDTO {
    private String name;
    private String sku;
    private ProductType type;
    private BigDecimal price;
    private int minimumStock;
    private SupplierSummaryDTO supplier;

    public ProductResponseDTO(Product product) {
        this.name = product.getName();
        this.sku = product.getSku();
        this.type = product.getType();
        this.price = product.getPrice();
        this.minimumStock = product.getMinimumStock();
        this.supplier = new SupplierSummaryDTO(product.getSupplier());
    }
}
