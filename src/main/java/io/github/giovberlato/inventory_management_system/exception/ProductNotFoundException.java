package io.github.giovberlato.inventory_management_system.exception;

// Exception thrown if there is no Product matching the criteria set for search/filter.
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
      super(message);
    }
}
