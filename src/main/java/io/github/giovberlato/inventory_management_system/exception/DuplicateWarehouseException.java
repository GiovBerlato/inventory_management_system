package io.github.giovberlato.inventory_management_system.exception;

public class DuplicateWarehouseException extends RuntimeException {
    public DuplicateWarehouseException(String message) {
        super(message);
    }
}
