package io.github.giovberlato.inventory_management_system.model;

import io.github.giovberlato.inventory_management_system.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "StockEntries")
public class StockEntry {
        @Id
        @GeneratedValue
        @Column(unique = true)
        @Setter(AccessLevel.NONE)
        @EqualsAndHashCode.Include
        private UUID id;
        @NotNull
        @ManyToOne(optional = false)
        @JoinColumn(name = "product_id", nullable = false)
        private Product product; // product can have many stock entries
        @ManyToOne(optional = false)
        @JoinColumn(name = "warehouse_id", nullable = false)
        private Warehouse warehouse; // many stock entries can be in one warehouse
        @NotNull
        @Min(0)
        private Integer quantity;
}