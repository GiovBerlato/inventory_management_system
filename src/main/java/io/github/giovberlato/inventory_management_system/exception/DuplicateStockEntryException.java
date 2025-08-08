package io.github.giovberlato.inventory_management_system.exception;

public class DuplicateStockEntryException extends RuntimeException {
    public DuplicateStockEntryException(String message) {
        super(message);
    }
}
