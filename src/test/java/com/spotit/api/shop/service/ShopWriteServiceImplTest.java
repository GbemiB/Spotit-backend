package com.spotit.api.shop.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.service.LevelDefinitionService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.shop.dto.RedeemResponse;
import com.spotit.api.shop.entity.OrderStatus;
import com.spotit.api.shop.entity.Product;
import com.spotit.api.shop.entity.ShopOrder;
import com.spotit.api.shop.repository.ProductRepository;
import com.spotit.api.shop.repository.ShopOrderRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopWriteServiceImplTest {

    @Mock ProductRepository productRepository;
    @Mock ShopOrderRepository shopOrderRepository;
    @Mock UserRepository userRepository;
    @Mock PointsWriteService pointsWriteService;
    @Mock LevelDefinitionService levelDefinitionService;

    ShopWriteServiceImpl service;
    UUID userId;

    @BeforeEach
    void setUp() {
        service = new ShopWriteServiceImpl(productRepository, shopOrderRepository, userRepository, pointsWriteService, levelDefinitionService);
        userId = UUID.randomUUID();
    }

    private void stubUser(long points, boolean premium) {
        User user = User.builder().id(userId).points(points).premium(premium).build();
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
    }

    // Only stubbed by tests that actually reach the level check (redeem() throws NOT_FOUND for
    // an unknown/inactive product before ever getting there) — matches the original hardcoded
    // LEVELS/LEVEL_ORDER exactly, now sourced from the (mocked) DB-backed service instead.
    private void stubLevels() {
        List<LevelUtil.LevelDef> defs = List.of(
                new LevelUtil.LevelDef("Blush", 0, 500),
                new LevelUtil.LevelDef("Petal", 500, 2000),
                new LevelUtil.LevelDef("Rosé", 2000, 5000),
                new LevelUtil.LevelDef("Bloom", 5000, 10000),
                new LevelUtil.LevelDef("Wildflower", 10000, 25000),
                new LevelUtil.LevelDef("Moonflower", 25000, 50000)
        );
        when(levelDefinitionService.getLevelDefs()).thenReturn(defs);
        when(levelDefinitionService.getLevelOrder())
                .thenReturn(List.of("Blush", "Petal", "Rosé", "Bloom", "Wildflower", "Moonflower", LevelUtil.MAX_LEVEL_NAME));
    }

    private Product activeProduct(int cost, String minLevel, boolean premiumOnly) {
        return Product.builder().id("rosewater_mist").name("Rosewater Face Mist")
                .cost(cost).minLevel(minLevel).premiumOnly(premiumOnly).active(true).build();
    }

    @Test
    void redeemingAnUnknownProductIsRejected() {
        stubUser(10_000, true);
        when(productRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.redeem(userId, "missing"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void redeemingAnInactiveProductIsTreatedAsNotFound() {
        stubUser(10_000, true);
        Product inactive = activeProduct(800, "Petal", false);
        inactive.setActive(false);
        when(productRepository.findById("rosewater_mist")).thenReturn(Optional.of(inactive));

        assertThatThrownBy(() -> service.redeem(userId, "rosewater_mist"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void redeemingBelowTheRequiredLevelIsRejected() {
        stubLevels();
        stubUser(0, false); // 0 points -> Blush, product needs Petal
        Product product = activeProduct(800, "Petal", false);
        when(productRepository.findById("rosewater_mist")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.redeem(userId, "rosewater_mist"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.LEVEL_TOO_LOW);
        verifyNoInteractions(pointsWriteService);
    }

    @Test
    void redeemingAPremiumOnlyItemWithoutPremiumIsRejected() {
        stubLevels();
        stubUser(10_000, false); // Wildflower level, but not premium
        Product product = activeProduct(4000, "Bloom", true);
        when(productRepository.findById("sheet_mask_set")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.redeem(userId, "sheet_mask_set"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.PREMIUM_REQUIRED);
        verifyNoInteractions(pointsWriteService);
    }

    @Test
    void redeemingWithoutEnoughPointsIsRejected() {
        stubLevels();
        stubUser(600, false); // Blush->Petal boundary but below the 800 cost
        Product product = activeProduct(800, "Petal", false);
        when(productRepository.findById("rosewater_mist")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.redeem(userId, "rosewater_mist"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
        verifyNoInteractions(pointsWriteService);
    }

    @Test
    void aQualifyingRedeemDeductsPointsAndCreatesAnOrder() {
        stubLevels();
        stubUser(1000, false);
        Product product = activeProduct(800, "Petal", false);
        when(productRepository.findById("rosewater_mist")).thenReturn(Optional.of(product));
        when(pointsWriteService.adjust(eq(userId), eq(-800), anyString(), anyString())).thenReturn(200L);
        when(shopOrderRepository.save(any(ShopOrder.class))).thenAnswer(invocation -> {
            ShopOrder order = invocation.getArgument(0);
            order.setId(UUID.randomUUID());
            return order;
        });

        RedeemResponse response = service.redeem(userId, "rosewater_mist");

        assertThat(response.productId()).isEqualTo("rosewater_mist");
        assertThat(response.pointsSpent()).isEqualTo(800);
        assertThat(response.newBalance()).isEqualTo(200);
        assertThat(response.status()).isEqualTo(OrderStatus.processing.name());
        verify(shopOrderRepository).save(argThat(order ->
                order.getUserId().equals(userId) && order.getProductId().equals("rosewater_mist")));
    }
}
