package io.github.giovberlato.inventory_management_system.exception;

// exception for when an addition/adjustment to a stock entry makes the warehouse exceed its full capacity.
public class WarehouseIsFullException extends RuntimeException {
    public WarehouseIsFullException(String message) {
        super(message);
    }
}
