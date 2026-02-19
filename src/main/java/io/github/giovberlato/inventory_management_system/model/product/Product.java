package io.github.giovberlato.inventory_management_system.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.giovberlato.inventory_management_system.model.StockEntry;

import io.github.giovberlato.inventory_management_system.model.Supplier;
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
        @Enumerated(EnumType.STRING)
        private ProductType type;
        @NotNull
        @DecimalMin("0.00")
        private BigDecimal price;
        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<StockEntry> stockEntries;
        @NotNull
        @Min(0)
        @Column(name = "min_stock")
        private Integer minimumStock;
        @ManyToOne
        @JoinColumn(name = "supplier_id")
        @NotNull
        @JsonBackReference
        private Supplier supplier;

        // Constructor used in unit testing
        public Product(String name, String sku, ProductType type, Integer minimumStock, Supplier supplier) {
                this.minimumStock = minimumStock;
                this.stockEntries = new ArrayList<>();
                this.price = new BigDecimal("10.89");
                this.sku = sku;
                this.type = type;
                this.name = name;
        }

        public Product(String name, String sku, ProductType type, BigDecimal price, Integer minimumStock, Supplier supplier) {
                this.name = name;
                this.sku = sku;
                this.type = type;
                this.price = price;
                this.minimumStock = minimumStock;
                this.supplier = supplier;
        }

        public void updateSupplier(Supplier newSupplier) {
                if (this.supplier != null) {
                        this.supplier.getProducts().remove(this);
                }
                this.supplier = newSupplier;
                if (newSupplier != null) {
                        newSupplier.getProducts().add(this);
                }
        }
}
