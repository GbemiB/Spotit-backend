package com.spotit.api.shop.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.service.LevelDefinitionService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.shop.dto.CreateProductRequest;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.RedeemResponse;
import com.spotit.api.shop.dto.UpdateProductRequest;
import com.spotit.api.shop.entity.OrderStatus;
import com.spotit.api.shop.entity.Product;
import com.spotit.api.shop.entity.ShopOrder;
import com.spotit.api.shop.repository.ProductRepository;
import com.spotit.api.shop.repository.ShopOrderRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopWriteServiceImpl implements ShopWriteService {
    private final ProductRepository productRepository;
    private final ShopOrderRepository shopOrderRepository;
    private final UserRepository userRepository;
    private final PointsWriteService pointsWriteService;
    private final LevelDefinitionService levelDefinitionService;

    @Override
    @Transactional
    public RedeemResponse redeem(UUID userId, String productId) {
        User user = requireUserForUpdate(userId);
        Product product = productRepository.findById(productId)
                .filter(Product::isActive)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PRODUCT_NOT_FOUND));

        String levelName = LevelUtil.levelFor(user.getPoints(), levelDefinitionService.getLevelDefs()).name();
        if (!LevelUtil.meetsMinLevel(levelName, product.getMinLevel(), levelDefinitionService.getLevelOrder())) {
            throw new ApiException(ErrorCode.LEVEL_TOO_LOW, ErrorMessage.unlocksAtLevel(product.getMinLevel()));
        }
        if (product.isPremiumOnly() && !user.isPremium()) {
            throw new ApiException(ErrorCode.PREMIUM_REQUIRED, ErrorMessage.PREMIUM_REQUIRED);
        }
        if (user.getPoints() < product.getCost()) {
            throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
        }

        long newBalance = pointsWriteService.adjust(userId, -product.getCost(), "🎁", "Redeemed " + product.getName());

        ShopOrder order = shopOrderRepository.save(ShopOrder.builder()
                .userId(userId)
                .productId(product.getId())
                .pointsSpent(product.getCost())
                .status(OrderStatus.processing)
                .build());

        return new RedeemResponse(order.getId(), product.getId(), product.getCost(), newBalance, order.getStatus().name());
    }

    @Override
    @Transactional
    public ProductAdminResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsById(request.id())) {
            throw new ApiException(ErrorCode.RESOURCE_ALREADY_EXISTS, ErrorMessage.productAlreadyExists(request.id()));
        }
        Product product = Product.builder()
                .id(request.id())
                .name(request.name())
                .cost(request.cost())
                .minLevel(request.minLevel())
                .premiumOnly(request.premiumOnly())
                .icon(request.icon())
                .active(true)
                .build();
        productRepository.save(product);
        return toAdminResponse(product);
    }

    @Override
    @Transactional
    public ProductAdminResponse updateProduct(String productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PRODUCT_NOT_FOUND));
        if (request.name() != null) product.setName(request.name());
        if (request.cost() != null) product.setCost(request.cost());
        if (request.minLevel() != null) product.setMinLevel(request.minLevel());
        if (request.premiumOnly() != null) product.setPremiumOnly(request.premiumOnly());
        if (request.icon() != null) product.setIcon(request.icon());
        if (request.active() != null) product.setActive(request.active());
        productRepository.save(product);
        return toAdminResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(productId);
    }

    private ProductAdminResponse toAdminResponse(Product p) {
        return new ProductAdminResponse(p.getId(), p.getName(), p.getCost(), p.getMinLevel(), p.isPremiumOnly(), p.getIcon(), p.isActive());
    }

    private User requireUserForUpdate(UUID userId) {
        return userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
