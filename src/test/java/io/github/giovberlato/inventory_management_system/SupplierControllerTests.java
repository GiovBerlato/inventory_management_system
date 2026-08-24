package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.security.SecurityConfig;
import io.github.giovberlato.inventory_management_system.contract.SupplierRequestDTO;
import io.github.giovberlato.inventory_management_system.controller.SupplierController;
import io.github.giovberlato.inventory_management_system.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(SupplierController.class)
@Import(SecurityConfig.class)
public class SupplierControllerTests {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SupplierService supplierService;


    private SupplierRequestDTO validRequest() {
        return new SupplierRequestDTO(
                "Sony Electronics",
                "Tokyo, Japan",
                "+81-12345678",
                "contact@sony.com"
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void findSupplierByName_ShouldReturn200() {

        assertThat(
                mvc.get()
                        .uri("/ims/supplier")
                        .param("name", "Sony Electronics")
        ).hasStatusOk();

        verify(supplierService)
                .findSupplierByName("Sony Electronics");
    }

    @Test
    @WithMockUser(roles = "USER")
    void listAllProductsBySupplier_ShouldReturn200() {

        given(supplierService.listAllProductsBySupplier("Sony Electronics"))
                .willReturn(List.of());

        assertThat(
                mvc.get()
                        .uri("/ims/supplier/Sony Electronics")
        ).hasStatusOk();

        verify(supplierService)
                .listAllProductsBySupplier("Sony Electronics");
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSupplier_ShouldAllowManager() throws Exception {

        SupplierRequestDTO request = validRequest();

        assertThat(
                mvc.post()
                        .uri("/ims/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(supplierService)
                .addSupplier(any(SupplierRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addSupplier_ShouldAllowAdmin() throws Exception {

        SupplierRequestDTO request = validRequest();

        assertThat(
                mvc.post()
                        .uri("/ims/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(supplierService)
                .addSupplier(any(SupplierRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void addSupplier_ShouldForbidUser() throws Exception {

        assertThat(
                mvc.post()
                        .uri("/ims/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest()))
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(supplierService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSupplier_ShouldAllowAdmin() {

        assertThat(
                mvc.delete()
                        .uri("/ims/supplier/Sony Electronics")
        ).hasStatus(HttpStatus.NO_CONTENT);

        verify(supplierService)
                .deleteSupplier("Sony Electronics");
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteSupplier_ShouldForbidManager() {

        assertThat(
                mvc.delete()
                        .uri("/ims/supplier/Sony Electronics")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(supplierService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteSupplier_ShouldForbidUser() {

        assertThat(
                mvc.delete()
                        .uri("/ims/supplier/Sony Electronics")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(supplierService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSupplier_ShouldReturn400_WhenNameIsBlank() throws Exception {

        SupplierRequestDTO request = new SupplierRequestDTO(
                "",
                "Tokyo, Japan",
                "+81-12345678",
                "contact@sony.com"
        );

        assertThat(
                mvc.post()
                        .uri("/ims/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(supplierService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSupplier_ShouldReturn400_WhenAddressIsBlank() throws Exception {

        SupplierRequestDTO request = new SupplierRequestDTO(
                "Sony Electronics",
                "",
                "+81-12345678",
                "contact@sony.com"
        );

        assertThat(
                mvc.post()
                        .uri("/ims/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(supplierService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSupplier_ShouldReturn400_WhenContactNumberIsBlank() throws Exception {

        SupplierRequestDTO request = new SupplierRequestDTO(
                "Sony Electronics",
                "Tokyo, Japan",
                "",
                "contact@sony.com"
        );

        assertThat(
                mvc.post()
                        .uri("/ims/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(supplierService);
    }

    @Test
    void findSupplier_ShouldReturn401_WhenUnauthenticated() {

        assertThat(
                mvc.get()
                        .uri("/ims/supplier")
                        .param("name", "Sony Electronics")
        ).hasStatus(HttpStatus.UNAUTHORIZED);

        verifyNoInteractions(supplierService);
    }
}
