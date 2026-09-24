package com.spotit.api.shop.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.service.LevelDefinitionService;
import com.spotit.api.shop.dto.OrderResponse;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.ProductResponse;
import com.spotit.api.shop.entity.OrderStatus;
import com.spotit.api.shop.entity.Product;
import com.spotit.api.shop.entity.ShopOrder;
import com.spotit.api.shop.repository.ProductRepository;
import com.spotit.api.shop.repository.ShopOrderRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopReadServiceImplTest {
    @Mock ProductRepository productRepository;
    @Mock ShopOrderRepository shopOrderRepository;
    @Mock UserRepository userRepository;
    @Mock LevelDefinitionService levelDefinitionService;
    @InjectMocks ShopReadServiceImpl service;

    UUID userId = UUID.randomUUID();

    private Product product(String id, String minLevel, boolean premiumOnly) {
        return Product.builder().id(id).name(id).cost(100).minLevel(minLevel).premiumOnly(premiumOnly).icon("gift").active(true).build();
    }

    private void stubUserAtBlush(boolean premium) {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).points(100).premium(premium).build()));
        when(levelDefinitionService.getLevelDefs()).thenReturn(List.of(
                new LevelUtil.LevelDef("Blush", 0, 500), new LevelUtil.LevelDef("Petal", 500, 2000)));
        when(levelDefinitionService.getLevelOrder()).thenReturn(List.of("Blush", "Petal", LevelUtil.MAX_LEVEL_NAME));
    }

    @Test
    void productsAreLockedByLevelAndPremium() {
        stubUserAtBlush(false);
        when(productRepository.findByActiveTrue()).thenReturn(List.of(
                product("sticker", "Blush", false),
                product("mist", "Petal", false),
                product("theme", "Blush", true)));

        List<ProductResponse> products = service.listProducts(userId);

        assertThat(products).extracting(ProductResponse::id, ProductResponse::locked, ProductResponse::lockReason).containsExactly(
                org.assertj.core.groups.Tuple.tuple("sticker", false, null),
                org.assertj.core.groups.Tuple.tuple("mist", true, "level_too_low"),
                org.assertj.core.groups.Tuple.tuple("theme", true, "premium_required"));
    }

    @Test
    void premiumUsersCanSeePremiumProductsUnlocked() {
        stubUserAtBlush(true);
        when(productRepository.findByActiveTrue()).thenReturn(List.of(product("theme", "Blush", true)));

        assertThat(service.listProducts(userId)).singleElement().satisfies(p -> {
            assertThat(p.locked()).isFalse();
            assertThat(p.lockReason()).isNull();
        });
    }

    @Test
    void productsForAnUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.listProducts(userId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void ordersAreMappedNewestFirst() {
        UUID orderId = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-09-20T10:00:00Z");
        when(shopOrderRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(ShopOrder.builder()
                .id(orderId).userId(userId).productId("mist").pointsSpent(100).status(OrderStatus.processing).createdAt(createdAt).build()));

        assertThat(service.listOrders(userId)).containsExactly(new OrderResponse(orderId, "mist", "processing", createdAt));
    }

    @Test
    void adminListIncludesInactiveProductsAndIcons() {
        when(productRepository.findAll()).thenReturn(List.of(product("mist", "Petal", false)));

        assertThat(service.listAllForAdmin()).containsExactly(new ProductAdminResponse("mist", "mist", 100, "Petal", false, "gift", true));
    }

    @Test
    void adminGetReturnsOneProduct() {
        when(productRepository.findById("mist")).thenReturn(Optional.of(product("mist", "Petal", false)));

        assertThat(service.getForAdmin("mist").minLevel()).isEqualTo("Petal");
    }

    @Test
    void adminGetForAnUnknownIdIs404() {
        when(productRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getForAdmin("nope")).isInstanceOf(ApiException.class);
    }
}
