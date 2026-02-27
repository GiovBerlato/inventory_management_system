package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.Warehouse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WarehouseRequestDTO {
    @NotBlank(message = "Warehouse must have a name")
    private String name;
    @NotBlank(message = "Warehouse must have a location")
    private String location;
    @Min(value = 1, message = "Warehouse must have a valid max capacity that is over zero.")
    private int maxCapacity;

    public WarehouseRequestDTO(Warehouse warehouse) {
        this.name = warehouse.getName();
        this.location = warehouse.getLocation();
        this.maxCapacity = warehouse.getMaxCapacity();
    }
}
