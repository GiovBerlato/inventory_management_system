package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.StockEntry;
import io.github.giovberlato.inventory_management_system.model.Warehouse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WarehouseResponseDTO {
    private String name;
    private String location;
    private int maxCapacity;
    private List<StockEntryResponseDTO> stockEntries;

    public WarehouseResponseDTO(Warehouse warehouse) {
        this.name = warehouse.getName();
        this.location = warehouse.getLocation();
        this.maxCapacity = warehouse.getMaxCapacity();
        this.stockEntries = warehouse.getStockEntries()
                                    .stream()
                                    .map(StockEntryResponseDTO::new)
                                    .toList();
    }
}
