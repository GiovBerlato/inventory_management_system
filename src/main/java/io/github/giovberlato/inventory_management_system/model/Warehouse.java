package io.github.giovberlato.inventory_management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "stockEntries"})
@NoArgsConstructor
@JsonIgnoreProperties({"stockEntries"})
public class Warehouse {
        @Id
        @GeneratedValue
        @Column(unique = true)
        @Setter(AccessLevel.NONE)
        private UUID id;
        @NotBlank
        private String name;
        @NotBlank
        private String location;
        @NotNull
        private Integer maxCapacity;
        @NotNull
        @Min(0)
        private Integer currentQuantity = 0;
        @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<StockEntry> stockEntries;



        /* Constructors for testing */

        public Warehouse(String name, String location, Integer maxCapacity) {
                this.name = name;
                this.location = location;
                this.maxCapacity = maxCapacity;
        }

        public Warehouse(UUID id, String name, String location, Integer maxCapacity) {
                this.id = id;
                this.name = name;
                this.location = location;
                this.maxCapacity = maxCapacity;
        }
}
