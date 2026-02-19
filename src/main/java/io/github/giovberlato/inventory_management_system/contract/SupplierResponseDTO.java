package io.github.giovberlato.inventory_management_system.contract;


import io.github.giovberlato.inventory_management_system.model.Supplier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SupplierResponseDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String contactNumber;
    private String email;
    @NotNull
    private List<ProductResponseDTO> products;

    public SupplierResponseDTO(Supplier supplier) {
        this.name = supplier.getName();
        this.address = supplier.getAddress();
        this.contactNumber = supplier.getContactNumber();
        this.email = supplier.getEmail();
        this.products = supplier.getProducts().stream()
                                            .map(ProductResponseDTO::new)
                                            .toList();
    }
}
