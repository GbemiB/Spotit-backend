package com.spotit.api.shop.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.service.LevelDefinitionService;
import com.spotit.api.shop.dto.OrderResponse;
import com.spotit.api.shop.dto.ProductAdminResponse;
import com.spotit.api.shop.dto.ProductResponse;
import com.spotit.api.shop.entity.Product;
import com.spotit.api.shop.repository.ProductRepository;
import com.spotit.api.shop.repository.ShopOrderRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopReadServiceImpl implements ShopReadService {

    private final ProductRepository productRepository;
    private final ShopOrderRepository shopOrderRepository;
    private final UserRepository userRepository;
    private final LevelDefinitionService levelDefinitionService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> listProducts(UUID userId) {
        User user = requireUser(userId);
        String levelName = LevelUtil.levelFor(user.getPoints(), levelDefinitionService.getLevelDefs()).name();
        List<String> levelOrder = levelDefinitionService.getLevelOrder();

        return productRepository.findByActiveTrue().stream()
                .map(p -> toResponse(p, levelName, levelOrder, user.isPremium()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> listOrders(UUID userId) {
        return shopOrderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(o -> new OrderResponse(o.getId(), o.getProductId(), o.getStatus().name(), o.getCreatedAt()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAdminResponse> listAllForAdmin() {
        return productRepository.findAll().stream().map(this::toAdminResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductAdminResponse getForAdmin(String productId) {
        return productRepository.findById(productId)
                .map(this::toAdminResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PRODUCT_NOT_FOUND));
    }

    private ProductResponse toResponse(Product p, String levelName, List<String> levelOrder, boolean premium) {
        boolean levelOk = LevelUtil.meetsMinLevel(levelName, p.getMinLevel(), levelOrder);
        boolean premiumOk = !p.isPremiumOnly() || premium;
        boolean locked = !levelOk || !premiumOk;
        String reason = !levelOk ? "level_too_low" : (!premiumOk ? "premium_required" : null);
        return new ProductResponse(p.getId(), p.getName(), p.getCost(), p.getMinLevel(), p.isPremiumOnly(), locked, reason);
    }

    private ProductAdminResponse toAdminResponse(Product p) {
        return new ProductAdminResponse(p.getId(), p.getName(), p.getCost(), p.getMinLevel(), p.isPremiumOnly(), p.getIcon(), p.isActive());
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
