package io.github.giovberlato.inventory_management_system.contract;

import io.github.giovberlato.inventory_management_system.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SupplierSummaryDTO {
    private String name;
    private String address;
    private String contactNumber;
    private String email;

    public SupplierSummaryDTO(Supplier supplier) {
        this.name = supplier.getName();
        this.address = supplier.getAddress();
        this.contactNumber = supplier.getContactNumber();
        this.email = supplier.getEmail();
    }
}
