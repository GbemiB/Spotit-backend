package com.spotit.api.shop.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.service.LevelDefinitionService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.shop.dto.CreateProductRequest;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.UpdateProductRequest;
import com.spotit.api.shop.entity.Product;
import com.spotit.api.shop.repository.ProductRepository;
import com.spotit.api.shop.repository.ShopOrderRepository;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Admin catalogue management in ShopWriteServiceImpl (redeem is covered by ShopWriteServiceImplTest). */
@ExtendWith(MockitoExtension.class)
class ShopWriteServiceImplAdminTest {
    @Mock ProductRepository productRepository;
    @Mock ShopOrderRepository shopOrderRepository;
    @Mock UserRepository userRepository;
    @Mock PointsWriteService pointsWriteService;
    @Mock LevelDefinitionService levelDefinitionService;
    @InjectMocks ShopWriteServiceImpl service;

    private Product mist() {
        return Product.builder().id("mist").name("Rosewater Mist").cost(800).minLevel("Rosé").premiumOnly(false).icon("spray").active(true).build();
    }

    @Test
    void createAddsAnActiveProduct() {
        when(productRepository.existsById("mist")).thenReturn(false);

        ProductAdminResponse response = service.createProduct(new CreateProductRequest("mist", "Rosewater Mist", 800, "Rosé", true, "spray"));

        assertThat(response).isEqualTo(new ProductAdminResponse("mist", "Rosewater Mist", 800, "Rosé", true, "spray", true));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createWithAnExistingIdIsRejected() {
        when(productRepository.existsById("mist")).thenReturn(true);

        assertThatThrownBy(() -> service.createProduct(new CreateProductRequest("mist", "Mist", 800, "Rosé", false, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.RESOURCE_ALREADY_EXISTS);
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateOnlyChangesTheFieldsThatWereSent() {
        Product product = mist();
        when(productRepository.findById("mist")).thenReturn(Optional.of(product));

        service.updateProduct("mist", new UpdateProductRequest(null, 650, null, null, null, null));

        assertThat(product.getCost()).isEqualTo(650);
        assertThat(product.getName()).isEqualTo("Rosewater Mist");
        assertThat(product.isActive()).isTrue();
        verify(productRepository).save(product);
    }

    @Test
    void updateCanChangeEveryField() {
        when(productRepository.findById("mist")).thenReturn(Optional.of(mist()));

        assertThat(service.updateProduct("mist", new UpdateProductRequest("Mist", 500, "Petal", true, "drop", false)))
                .isEqualTo(new ProductAdminResponse("mist", "Mist", 500, "Petal", true, "drop", false));
    }

    @Test
    void updateForAnUnknownProductIs404() {
        when(productRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProduct("nope", new UpdateProductRequest("x", null, null, null, null, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void deleteRemovesAnExistingProduct() {
        when(productRepository.existsById("mist")).thenReturn(true);

        service.deleteProduct("mist");

        verify(productRepository).deleteById("mist");
    }

    @Test
    void deleteForAnUnknownProductIs404() {
        when(productRepository.existsById("nope")).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProduct("nope")).isInstanceOf(ApiException.class);
        verify(productRepository, never()).deleteById(anyString());
    }
}
