package io.github.giovberlato.inventory_management_system.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.giovberlato.inventory_management_system.model.StockEntry;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "stockEntries"})
@NoArgsConstructor
@JsonIgnoreProperties({"stockEntries"})
public class Product {
        @Id
        @GeneratedValue
        @Column(unique = true)
        @Setter(AccessLevel.NONE)
        private UUID id;
        @NotBlank
        @Column(unique = true)
        private String name;
        @NotBlank
        @Column(unique = true)
        private String sku;
        @NotNull
        private ProductType type;
        @NotNull
        @DecimalMin("0.00")
        private BigDecimal price;
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<StockEntry> stockEntries;
        @NotNull
        @Min(0)
        @Column(name = "min_stock")
        private Integer minimumStock;

        // Constructor used in unit testing
        public Product(String name, String sku, ProductType type, Integer minimumStock) {
                this.minimumStock = minimumStock;
                this.stockEntries = new ArrayList<>();
                this.price = new BigDecimal("10.89");
                this.sku = sku;
                this.type = type;
                this.name = name;
        }
}
