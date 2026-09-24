package com.spotit.api.shop.controller;

import com.spotit.api.shop.dto.CreateProductRequest;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.UpdateProductRequest;
import com.spotit.api.shop.service.ShopReadService;
import com.spotit.api.shop.service.ShopWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductConfigControllerTest {
    @Mock ShopReadService shopReadService;
    @Mock ShopWriteService shopWriteService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new ProductConfigController(shopReadService, shopWriteService));
    }

    private ProductAdminResponse theme(int cost) {
        return new ProductAdminResponse("theme_dark_rose", "Dark Rose Theme", cost, "Petal", false, "palette", true);
    }

    @Test
    void listReturnsEveryProduct() throws Exception {
        when(shopReadService.listAllForAdmin()).thenReturn(List.of(theme(300)));

        mvc.perform(get("/api/v1/config/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("theme_dark_rose"));
    }

    @Test
    void getReturnsOneProduct() throws Exception {
        when(shopReadService.getForAdmin("theme_dark_rose")).thenReturn(theme(300));

        mvc.perform(get("/api/v1/config/products/theme_dark_rose"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cost").value(300));
    }

    @Test
    void createReturns201() throws Exception {
        CreateProductRequest request = new CreateProductRequest("theme_dark_rose", "Dark Rose Theme", 300, "Petal", false, "palette");
        when(shopWriteService.createProduct(request)).thenReturn(theme(300));

        mvc.perform(post("/api/v1/config/products").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createRejectsAZeroCost() throws Exception {
        mvc.perform(post("/api/v1/config/products").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CreateProductRequest("x", "X", 0, "Petal", false, null))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(shopWriteService);
    }

    @Test
    void updateChangesThePrice() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest(null, 250, null, null, null, null);
        when(shopWriteService.updateProduct("theme_dark_rose", request)).thenReturn(theme(250));

        mvc.perform(patch("/api/v1/config/products/theme_dark_rose").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cost").value(250));
    }

    @Test
    void deleteRemovesTheProduct() throws Exception {
        mvc.perform(delete("/api/v1/config/products/theme_dark_rose"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product deleted."));

        verify(shopWriteService).deleteProduct("theme_dark_rose");
    }
}
