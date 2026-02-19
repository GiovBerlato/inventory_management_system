package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseSummaryDTO {
    private String name;
    private String location;
    private int maxCapacity;

    public WarehouseSummaryDTO(Warehouse warehouse) {
        this.name = warehouse.getName();
        this.location = warehouse.getLocation();
        this.maxCapacity = warehouse.getMaxCapacity();
    }
}
