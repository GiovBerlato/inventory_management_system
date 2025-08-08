package io.github.giovberlato.inventory_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // PRODUCT EXCEPTIONS
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<Object> handleDuplicateProductException(DuplicateProductException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // WAREHOUSE EXCEPTIONS
    @ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity<Object> handleWarehouseNotFoundException(WarehouseNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateWarehouseException.class)
    public ResponseEntity<Object> handleDuplicateWarehouseException(DuplicateWarehouseException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(WarehouseIsFullException.class)
    public ResponseEntity<Object> handleWarehouseIsFullException(WarehouseIsFullException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // STOCK ENTRY EXCEPTIONS
    @ExceptionHandler(StockEntryNotFoundException.class)
    public ResponseEntity<Object> handleStockEntryNotFound(StockEntryNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateStockEntryException.class)
    public ResponseEntity<Object> handleDuplicateStockEntryException(DuplicateStockEntryException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
