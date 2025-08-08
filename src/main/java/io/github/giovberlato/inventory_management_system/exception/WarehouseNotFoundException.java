package io.github.giovberlato.inventory_management_system.exception;

public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
