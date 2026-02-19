package io.github.giovberlato.inventory_management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.giovberlato.inventory_management_system.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@JsonIgnoreProperties("products")
public class Supplier {
    @Id
    @GeneratedValue
    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String contactNumber;
    private String email;
    @NotNull
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products;
}
