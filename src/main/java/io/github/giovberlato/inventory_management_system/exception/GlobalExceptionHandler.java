package io.github.giovberlato.inventory_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Product Exceptions

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(
            ProductNotFoundException ex) {

        return problem(
                HttpStatus.NOT_FOUND,
                "Product not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ProblemDetail handleDuplicateProduct(
            DuplicateProductException ex) {

        return problem(
                HttpStatus.CONFLICT,
                "Product already exists",
                ex.getMessage()
        );
    }

    // Warehouse Exceptions

    @ExceptionHandler(WarehouseNotFoundException.class)
    public ProblemDetail handleWarehouseNotFound(
            WarehouseNotFoundException ex) {

        return problem(
                HttpStatus.NOT_FOUND,
                "Warehouse not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateWarehouseException.class)
    public ProblemDetail handleDuplicateWarehouse(
            DuplicateWarehouseException ex) {

        return problem(
                HttpStatus.CONFLICT,
                "Warehouse already exists",
                ex.getMessage()
        );
    }

    @ExceptionHandler(WarehouseIsFullException.class)
    public ProblemDetail handleWarehouseIsFull(
            WarehouseIsFullException ex) {

        return problem(
                HttpStatus.CONFLICT,
                "Warehouse capacity exceeded",
                ex.getMessage()
        );
    }

    // Stock Exceptions

    @ExceptionHandler(StockEntryNotFoundException.class)
    public ProblemDetail handleStockEntryNotFound(
            StockEntryNotFoundException ex) {

        return problem(
                HttpStatus.NOT_FOUND,
                "Stock entry not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateStockEntryException.class)
    public ProblemDetail handleDuplicateStockEntry(
            DuplicateStockEntryException ex) {

        return problem(
                HttpStatus.CONFLICT,
                "Stock entry already exists",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleInsufficientStock(
            InsufficientStockException ex) {

        return problem(
                HttpStatus.CONFLICT,
                "Insufficient stock",
                ex.getMessage()
        );
    }

    // Supplier Exceptions

    @ExceptionHandler(SupplierNotFoundException.class)
    public ProblemDetail handleSupplierNotFound(
            SupplierNotFoundException ex) {

        return problem(
                HttpStatus.NOT_FOUND,
                "Supplier not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateSupplierException.class)
    public ProblemDetail handleDuplicateSupplier(
            DuplicateSupplierException ex) {

        return problem(
                HttpStatus.CONFLICT,
                "Supplier already exists",
                ex.getMessage()
        );
    }

    // Standardize Bean Validation Errors

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Request contains invalid fields"
                );

        problem.setTitle("Validation failed");

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        problem.setProperty("errors", errors);

        return problem;
    }

    // ProblemDetail helper

    private ProblemDetail problem(
            HttpStatus status,
            String title,
            String detail) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);

        return problem;
    }
}