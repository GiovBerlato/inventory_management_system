package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.security.SecurityConfig;
import io.github.giovberlato.inventory_management_system.contract.StockEntryAdjustmentDTO;
import io.github.giovberlato.inventory_management_system.contract.StockEntryRequestDTO;
import io.github.giovberlato.inventory_management_system.controller.StockEntryController;
import io.github.giovberlato.inventory_management_system.service.StockEntryService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(StockEntryController.class)
@Import(SecurityConfig.class)
public class StockEntryControllerTests {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockEntryService stockEntryService;


    private StockEntryRequestDTO validStockRequest() {
        return new StockEntryRequestDTO(
                "SONY-PS5",
                "Central Warehouse",
                "Sony",
                100
        );
    }

    private StockEntryAdjustmentDTO validAdjustment() {
        return new StockEntryAdjustmentDTO(
                "SONY-PS5",
                "Central Warehouse",
                50
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void listAllStocksInWarehouse_ShouldReturn200() {
        given(stockEntryService.listAllStocksInWarehouse("Central Warehouse"))
                .willReturn(List.of());

        assertThat(
                mvc.get()
                        .uri("/ims/stock/warehouse")
                        .param("name", "Central Warehouse")
        ).hasStatusOk();

        verify(stockEntryService)
                .listAllStocksInWarehouse("Central Warehouse");
    }

    @Test
    @WithMockUser(roles = "USER")
    void listAllStocksForProduct_ShouldReturn200() {
        given(stockEntryService.listAllStocksForProduct("SONY-PS5"))
                .willReturn(List.of());

        assertThat(
                mvc.get()
                        .uri("/ims/stock/products")
                        .param("sku", "SONY-PS5")
        ).hasStatusOk();

        verify(stockEntryService)
                .listAllStocksForProduct("SONY-PS5");
    }

    @Test
    @WithMockUser(roles = "USER")
    void getStockForProductInWarehouse_ShouldReturn200() {
        assertThat(
                mvc.get()
                        .uri("/ims/stock/Central Warehouse/SONY-PS5")
        ).hasStatusOk();

        verify(stockEntryService)
                .getStockForProductInWarehouse(
                        "Central Warehouse",
                        "SONY-PS5"
                );
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addStockEntry_ShouldAllowManager() throws Exception {
        StockEntryRequestDTO request = validStockRequest();

        assertThat(
                mvc.post()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(stockEntryService)
                .addStockEntry(any(StockEntryRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addStockEntry_ShouldAllowAdmin() throws Exception {
        StockEntryRequestDTO request = validStockRequest();

        assertThat(
                mvc.post()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(stockEntryService)
                .addStockEntry(any(StockEntryRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void addStockEntry_ShouldForbidUser() throws Exception {
        assertThat(
                mvc.post()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStockRequest()))
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(stockEntryService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void adjustStock_ShouldAllowManager() throws Exception {
        StockEntryAdjustmentDTO request = validAdjustment();

        assertThat(
                mvc.patch()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        verify(stockEntryService)
                .adjustStock(any(StockEntryAdjustmentDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adjustStock_ShouldAllowAdmin() throws Exception {
        StockEntryAdjustmentDTO request = validAdjustment();

        assertThat(
                mvc.patch()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        verify(stockEntryService)
                .adjustStock(any(StockEntryAdjustmentDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void adjustStock_ShouldForbidUser() throws Exception {
        assertThat(
                mvc.patch()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAdjustment()))
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(stockEntryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteStockEntry_ShouldAllowAdminAndPassArgumentsInCorrectOrder() {
        assertThat(
                mvc.delete()
                        .uri("/ims/stock/Central Warehouse/SONY-PS5")
        ).hasStatus(HttpStatus.NO_CONTENT);

        verify(stockEntryService)
                .deleteStockEntry(
                        "SONY-PS5",
                        "Central Warehouse"
                );
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteStockEntry_ShouldForbidManager() {
        assertThat(
                mvc.delete()
                        .uri("/ims/stock/Central Warehouse/SONY-PS5")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(stockEntryService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteStockEntry_ShouldForbidUser() {
        assertThat(
                mvc.delete()
                        .uri("/ims/stock/Central Warehouse/SONY-PS5")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(stockEntryService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addStockEntry_ShouldReturn400_WhenQuantityIsNegative() throws Exception {
        StockEntryRequestDTO request = new StockEntryRequestDTO(
                "SONY-PS5",
                "Central Warehouse",
                "Sony",
                -10
        );

        assertThat(
                mvc.post()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(stockEntryService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addStockEntry_ShouldReturn400_WhenProductSkuIsBlank() throws Exception {
        StockEntryRequestDTO request = new StockEntryRequestDTO(
                "",
                "Central Warehouse",
                "Sony",
                100
        );

        assertThat(
                mvc.post()
                        .uri("/ims/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(stockEntryService);
    }

    @Test
    void listStocks_ShouldReturn401_WhenUnauthenticated() {
        assertThat(
                mvc.get()
                        .uri("/ims/stock/warehouse")
                        .param("name", "Central Warehouse")
        ).hasStatus(HttpStatus.UNAUTHORIZED);

        verifyNoInteractions(stockEntryService);
    }
}
