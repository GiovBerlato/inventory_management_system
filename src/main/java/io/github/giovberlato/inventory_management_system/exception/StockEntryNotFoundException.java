package io.github.giovberlato.inventory_management_system.exception;

public class StockEntryNotFoundException extends RuntimeException {
    public StockEntryNotFoundException(String message) {
        super(message);
    }
}
