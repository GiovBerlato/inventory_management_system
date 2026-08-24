package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.contract.ProductRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.ProductResponseDTO;
import io.github.giovberlato.inventory_management_system.contract.SupplierSummaryDTO;
import io.github.giovberlato.inventory_management_system.controller.ProductController;
import io.github.giovberlato.inventory_management_system.model.product.ProductType;
import io.github.giovberlato.inventory_management_system.security.SecurityConfig;
import io.github.giovberlato.inventory_management_system.service.ProductService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTests {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductRequestDTO validRequest() {
        return new ProductRequestDTO(
                "PlayStation 5",
                "SONY-PS5",
                ProductType.ELECTRONICS,
                new BigDecimal("499.99"),
                10,
                "Sony"
        );
    }

    private ProductResponseDTO response() {
        return new ProductResponseDTO(
                "PlayStation 5",
                "SONY-PS5",
                ProductType.ELECTRONICS,
                new BigDecimal("499.99"),
                10,
                new SupplierSummaryDTO(
                        "Supplier",
                        "Address",
                        "12341234",
                        "email@email.com"
                )
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void getProductBySKU_ShouldReturn200() {
        given(productService.searchBySku("SONY-PS5"))
                .willReturn(response());

        assertThat(
                mvc.get()
                        .uri("/ims/products/sku-search")
                        .param("sku", "SONY-PS5")
        ).hasStatusOk();

        verify(productService).searchBySku("SONY-PS5");
    }

    @Test
    @WithMockUser(roles = "USER")
    void listAll_ShouldReturn200() {
        given(productService.listAll())
                .willReturn(List.of(response()));

        assertThat(
                mvc.get()
                        .uri("/ims/products")
        ).hasStatusOk();

        verify(productService).listAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void listByType_ShouldReturn200() {
        given(productService.listAllByType(ProductType.ELECTRONICS))
                .willReturn(List.of(response()));

        assertThat(
                mvc.get()
                        .uri("/ims/products/filter/ELECTRONICS")
        ).hasStatusOk();

        verify(productService)
                .listAllByType(ProductType.ELECTRONICS);
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchByKeyword_ShouldReturn200() {
        given(productService.searchAllByNameContaining("PlayStation"))
                .willReturn(List.of(response()));

        assertThat(
                mvc.get()
                        .uri("/ims/products/keyword-search")
                        .param("keyword", "PlayStation")
        ).hasStatusOk();

        verify(productService)
                .searchAllByNameContaining("PlayStation");
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void createProduct_ShouldAllowManager() throws Exception {
        ProductRequestDTO request = validRequest();

        given(productService.addProduct(any(ProductRequestDTO.class)))
                .willReturn(response());

        assertThat(
                mvc.post()
                        .uri("/ims/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(productService)
                .addProduct(any(ProductRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_ShouldAllowAdmin() throws Exception {
        ProductRequestDTO request = validRequest();

        given(productService.addProduct(any(ProductRequestDTO.class)))
                .willReturn(response());

        assertThat(
                mvc.post()
                        .uri("/ims/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(productService)
                .addProduct(any(ProductRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createProduct_ShouldForbidUser() throws Exception {
        ProductRequestDTO request = validRequest();

        assertThat(
                mvc.post()
                        .uri("/ims/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateProduct_ShouldAllowManager() throws Exception {
        ProductRequestDTO request = validRequest();

        assertThat(
                mvc.put()
                        .uri("/ims/products/SONY-PS5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        verify(productService)
                .updateProduct(any(ProductRequestDTO.class), eq("SONY-PS5"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_ShouldAllowAdmin() throws Exception {
        ProductRequestDTO request = validRequest();

        assertThat(
                mvc.put()
                        .uri("/ims/products/SONY-PS5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        verify(productService)
                .updateProduct(any(ProductRequestDTO.class), eq("SONY-PS5"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateProduct_ShouldForbidUser() throws Exception {
        ProductRequestDTO request = validRequest();

        assertThat(
                mvc.put()
                        .uri("/ims/products/SONY-PS5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_ShouldAllowAdmin() {
        assertThat(
                mvc.delete()
                        .uri("/ims/products/SONY-PS5")
        ).hasStatus(HttpStatus.NO_CONTENT);

        verify(productService)
                .deleteProduct("SONY-PS5");
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteProduct_ShouldForbidManager() {
        assertThat(
                mvc.delete()
                        .uri("/ims/products/SONY-PS5")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteProduct_ShouldForbidUser() {
        assertThat(
                mvc.delete()
                        .uri("/ims/products/SONY-PS5")
        ).hasStatus(HttpStatus.FORBIDDEN);

        verifyNoInteractions(productService);
    }

    @Test
    void listAll_ShouldReturn401_WhenUnauthenticated() {
        assertThat(
                mvc.get()
                        .uri("/ims/products")
        ).hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void createProduct_ShouldReturn401_WhenUnauthenticated() throws Exception {
        assertThat(
                mvc.post()
                        .uri("/ims/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest()))
        ).hasStatus(HttpStatus.UNAUTHORIZED);
    }
}
