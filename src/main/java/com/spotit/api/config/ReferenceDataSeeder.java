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
                ContentItem.builder().tag("Education").title("Understanding your fertile window").imageKey("lifestyle").sponsored(false).sortOrder(1)
                        .body("""
                                Your fertile window is the stretch of each cycle when pregnancy is possible — typically the 5 days before ovulation through the day of ovulation itself.

                                Why that range? Sperm can survive in the body for up to 5 days, while an egg lives for only about 24 hours after release. So the "window" is really sperm waiting for an egg, not the other way around.

                                Spot it estimates your window from your average cycle length. You can narrow it down further by tracking signs your body already gives you:

                                • Cervical mucus that turns clear and stretchy, like raw egg white
                                • A slight rise in resting body temperature after ovulation
                                • Mild one-sided pelvic twinges ("mittelschmerz")

                                Cycles vary month to month, so treat this as a well-informed estimate, not a guarantee — and check in with a doctor if you're planning or avoiding pregnancy and want more precision.""")
                        .build(),
                ContentItem.builder().tag("Nutrition").title("5 foods that support ovulation").imageKey("food").sponsored(false).sortOrder(2)
                        .body("""
                                Diet alone won't control ovulation, but certain nutrients give your hormones the raw materials they need to function well:

                                • Leafy greens (spinach, kale) — folate, which supports healthy ovulation and early fetal development
                                • Berries — antioxidants that help protect egg cells from oxidative stress
                                • Fatty fish (salmon, sardines) — omega-3s linked to more regular cycles
                                • Whole grains (oats, quinoa) — steadier blood sugar, which helps keep reproductive hormones balanced
                                • Nuts and seeds — vitamin E and zinc, both tied to hormone production

                                None of these guarantee ovulation or a pregnancy outcome on their own — think of them as one supporting factor alongside sleep, stress, and overall health. A doctor or registered dietitian can tailor this to your specific needs.""")
                        .build(),
                ContentItem.builder().tag("Sponsored").title("Nourish prenatal multivitamins").imageKey("product").sponsored(true).advertiser("Nourish").sortOrder(3)
                        .body("""
                                A prenatal multivitamin formulated to help meet essential nutrient needs before and during pregnancy, including folate, iron, and vitamin D.

                                As with any supplement, check with your doctor before starting — especially if you take other medications, are breastfeeding, or manage an existing medical condition.""")
                        .build()
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
