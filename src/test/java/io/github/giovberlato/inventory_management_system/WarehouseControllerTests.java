package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.security.SecurityConfig;
import io.github.giovberlato.inventory_management_system.contract.WarehouseRequestDTO;
import io.github.giovberlato.inventory_management_system.controller.WarehouseController;
import io.github.giovberlato.inventory_management_system.service.WarehouseService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(WarehouseController.class)
@Import(SecurityConfig.class)
public class WarehouseControllerTests {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WarehouseService warehouseService;


    private WarehouseRequestDTO validRequest() {
        return new WarehouseRequestDTO(
                "Central Warehouse",
                "Sorocaba",
                5000
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void listAll_ShouldReturn200() {

        given(warehouseService.listAll())
                .willReturn(List.of());

        assertThat(
                mvc.get()
                        .uri("/ims/warehouses")
        ).hasStatusOk();

        verify(warehouseService).listAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchByName_ShouldReturn200() {

        assertThat(
                mvc.get()
                        .uri("/ims/warehouses/filter")
                        .param("name", "Central Warehouse")
        ).hasStatusOk();

        verify(warehouseService)
                .searchByName("Central Warehouse");
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchByKeyword_ShouldReturn200() {

        given(warehouseService.searchAllByNameContaining("Central"))
                .willReturn(List.of());

        assertThat(
                mvc.get()
                        .uri("/ims/warehouses/keyword-search")
                        .param("keyword", "Central")
        ).hasStatusOk();

        verify(warehouseService)
                .searchAllByNameContaining("Central");
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addWarehouse_ShouldAllowManager() throws Exception {

        WarehouseRequestDTO request = validRequest();

        assertThat(
                mvc.post()
                        .uri("/ims/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(warehouseService)
                .addWarehouse(any(WarehouseRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addWarehouse_ShouldAllowAdmin() throws Exception {

        WarehouseRequestDTO request = validRequest();

        assertThat(
                mvc.post()
                        .uri("/ims/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(warehouseService)
                .addWarehouse(any(WarehouseRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void addWarehouse_ShouldForbidUser() throws Exception {

        assertThat(
                mvc.post()
                        .uri("/ims/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest()))
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(warehouseService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateWarehouse_ShouldAllowManager() throws Exception {

        WarehouseRequestDTO request = new WarehouseRequestDTO(
                "Updated Warehouse",
                "Updated Location",
                7000
        );

        assertThat(
                mvc.put()
                        .uri("/ims/warehouses/Central Warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        verify(warehouseService)
                .updateWarehouse(
                        any(WarehouseRequestDTO.class),
                        eq("Central Warehouse")
                );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWarehouse_ShouldAllowAdmin() throws Exception {

        WarehouseRequestDTO request = new WarehouseRequestDTO(
                "Updated Warehouse",
                "Updated Location",
                7000
        );

        assertThat(
                mvc.put()
                        .uri("/ims/warehouses/Central Warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        verify(warehouseService)
                .updateWarehouse(
                        any(WarehouseRequestDTO.class),
                        eq("Central Warehouse")
                );
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateWarehouse_ShouldForbidUser() throws Exception {

        assertThat(
                mvc.put()
                        .uri("/ims/warehouses/Central Warehouse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest()))
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(warehouseService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWarehouse_ShouldAllowAdmin() {

        assertThat(
                mvc.delete()
                        .uri("/ims/warehouses/Central Warehouse")
        ).hasStatus(HttpStatus.NO_CONTENT);

        verify(warehouseService)
                .deleteWarehouse("Central Warehouse");
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteWarehouse_ShouldForbidManager() {

        assertThat(
                mvc.delete()
                        .uri("/ims/warehouses/Central Warehouse")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(warehouseService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteWarehouse_ShouldForbidUser() {

        assertThat(
                mvc.delete()
                        .uri("/ims/warehouses/Central Warehouse")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(warehouseService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addWarehouse_ShouldReturn400_WhenNameIsBlank() throws Exception {

        WarehouseRequestDTO request = new WarehouseRequestDTO(
                "",
                "Sorocaba",
                5000
        );

        assertThat(
                mvc.post()
                        .uri("/ims/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(warehouseService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addWarehouse_ShouldReturn400_WhenLocationIsBlank() throws Exception {

        WarehouseRequestDTO request = new WarehouseRequestDTO(
                "Central Warehouse",
                "",
                5000
        );

        assertThat(
                mvc.post()
                        .uri("/ims/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(warehouseService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addWarehouse_ShouldReturn400_WhenCapacityIsZero() throws Exception {

        WarehouseRequestDTO request = new WarehouseRequestDTO(
                "Central Warehouse",
                "Sorocaba",
                0
        );

        assertThat(
                mvc.post()
                        .uri("/ims/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(warehouseService);
    }

    @Test
    void listAll_ShouldReturn401_WhenUnauthenticated() {

        assertThat(
                mvc.get()
                        .uri("/ims/warehouses")
        ).hasStatus(HttpStatus.UNAUTHORIZED);

        verifyNoInteractions(warehouseService);
    }
}
