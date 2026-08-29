package com.spotit.api.config;

import com.spotit.api.content.entity.ContentItem;
import com.spotit.api.content.repository.ContentItemRepository;
import com.spotit.api.rewards.entity.BadgeDefinition;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import com.spotit.api.rewards.entity.LevelDefinition;
import com.spotit.api.rewards.repository.BadgeDefinitionRepository;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.LevelDefinitionRepository;
import com.spotit.api.shop.entity.Product;
import com.spotit.api.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Idempotently seeds the small set of reference/definition rows the app
 * needs to function (badge & challenge definitions, shop products, a starter
 * content feed) — the equivalent of what used to be a Flyway seed migration.
 */
@Component
@RequiredArgsConstructor
public class ReferenceDataSeeder implements ApplicationRunner {

    private final BadgeDefinitionRepository badgeDefinitionRepository;
    private final ChallengeDefinitionRepository challengeDefinitionRepository;
    private final ProductRepository productRepository;
    private final ContentItemRepository contentItemRepository;
    private final LevelDefinitionRepository levelDefinitionRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedBadges();
        seedChallenges();
        seedProducts();
        seedContent();
        seedLevels();
    }

    private void seedBadges() {
        if (badgeDefinitionRepository.count() > 0) return;
        badgeDefinitionRepository.saveAll(List.of(
                new BadgeDefinition("first_flow", "First Flow", "Log your first period"),
                new BadgeDefinition("cycle_veteran", "Cycle Veteran", "Log 28 or more days"),
                new BadgeDefinition("know_your_body", "Know Your Body", "Log 10 or more days"),
                new BadgeDefinition("week_warrior", "Week Warrior", "Reach a 7-day logging streak"),
                new BadgeDefinition("ovulation_oracle", "Ovulation Oracle", "3 LH-peak confirmations (requires the Fertility module)"),
                new BadgeDefinition("health_nerd", "Health Nerd", "Read 20 articles (requires the Education Hub)")
        ));
    }

    private void seedChallenges() {
        if (challengeDefinitionRepository.count() > 0) return;
        challengeDefinitionRepository.saveAll(List.of(
                ChallengeDefinition.builder().id("log_week").title("Log your mood every day").reward(5).total(7).type(ChallengeType.WEEKLY_LOG).build(),
                ChallengeDefinition.builder().id("read_3").title("Read 3 nutrition articles").reward(5).total(3).type(ChallengeType.STATIC).build(),
                ChallengeDefinition.builder().id("daily_log").title("Log today").reward(10).total(1).type(ChallengeType.STATIC).build()
        ));
    }

    private void seedProducts() {
        if (productRepository.count() > 0) return;
        productRepository.saveAll(List.of(
                Product.builder().id("rosewater_mist").name("Rosewater Face Mist").cost(5000).minLevel("Petal").premiumOnly(false).icon("🌹").active(true).build(),
                Product.builder().id("vitc_serum").name("Vitamin C Serum").cost(5000).minLevel("Rosé").premiumOnly(false).icon("💧").active(true).build(),
                Product.builder().id("sheet_mask_set").name("Hydrating Sheet Mask Set").cost(7500).minLevel("Bloom").premiumOnly(true).icon("🧖‍♀️").active(true).build(),
                Product.builder().id("skincare_bundle").name("Luxury Skincare Bundle").cost(20000).minLevel("Wildflower").premiumOnly(true).icon("🎁").active(true).build()
        ));
    }

    private void seedContent() {
        if (contentItemRepository.count() > 0) return;
        contentItemRepository.saveAll(List.of(
                ContentItem.builder().tag("Education").title("Understanding your fertile window").imageUrl("https://cdn.spotit.app/content/lifestyle.jpg").sponsored(false).sortOrder(1).build(),
                ContentItem.builder().tag("Nutrition").title("5 foods that support ovulation").imageUrl("https://cdn.spotit.app/content/food.jpg").sponsored(false).sortOrder(2).build(),
                ContentItem.builder().tag("Sponsored").title("Nourish prenatal multivitamins").imageUrl("https://cdn.spotit.app/content/product.jpg").sponsored(true).advertiser("Nourish").sortOrder(3).build()
        ));
    }

    private void seedLevels() {
        if (levelDefinitionRepository.count() > 0) return;
        levelDefinitionRepository.saveAll(List.of(
                LevelDefinition.builder().id("blush").name("Blush").pointsLow(0).pointsHigh(500).sortOrder(1).build(),
                LevelDefinition.builder().id("petal").name("Petal").pointsLow(500).pointsHigh(2000).sortOrder(2).build(),
                LevelDefinition.builder().id("rose").name("Rosé").pointsLow(2000).pointsHigh(5000).sortOrder(3).build(),
                LevelDefinition.builder().id("bloom").name("Bloom").pointsLow(5000).pointsHigh(10000).sortOrder(4).build(),
                LevelDefinition.builder().id("wildflower").name("Wildflower").pointsLow(10000).pointsHigh(25000).sortOrder(5).build(),
                LevelDefinition.builder().id("moonflower").name("Moonflower").pointsLow(25000).pointsHigh(50000).sortOrder(6).build()
        ));
    }
}
