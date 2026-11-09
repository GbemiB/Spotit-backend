package com.spotit.api.spotlight.core;

import org.junit.jupiter.api.Test;
import com.spotit.api.spotlight.core.entity.SpotlightRank;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.repository.SpotlightRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SpotlightCoreScriptTest {
    private static final String PAD_BANK_BLURB = "Serve each visitor the right pack. The first visitors on every level are free.";

    @Autowired DataSource dataSource;
    @Autowired SpotlightChallengeRepository challengeRepository;
    @Autowired SpotlightRankRepository rankRepository;
    @Autowired JdbcTemplate jdbc;

    @Test
    void theCoreScriptLoadsThreeLiveChallengesAndFourRanksAndCanBeRunTwice() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
        }

        assertThat(challengeRepository.findByActiveTrueOrderBySortOrderAsc()).extracting("id").containsExactly("know-yourself", "pad-bank-run", "give-day");
        assertThat(challengeRepository.count()).isEqualTo(3);
        assertThat(challengeRepository.findById("know-yourself")).get().extracting("partnerName").isEqualTo("Know {name} better");
        assertThat(challengeRepository.findById("give-day")).get().extracting("status", "freePlays").containsExactly("live", 1);
        assertThat(challengeRepository.findById("pad-bank-run")).get().extracting("status", "freePlays").containsExactly("live", 1);
        assertThat(challengeRepository.findById("pad-bank-run")).get().extracting("blurb").isEqualTo(PAD_BANK_BLURB);
        assertThat(rankRepository.findByActiveTrueOrderByMinStarsAsc()).extracting("minStars").containsExactly(0, 50, 100, 200);
    }

    @Test
    void theCoreScriptRaisesOldRankStarsButKeepsAdminChanges() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
            setStars("explorer", 15);
            setStars("myth-buster", 45);
            setStars("know-it-all", 120);
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
        }

        assertThat(rankRepository.findByActiveTrueOrderByMinStarsAsc()).extracting("minStars").containsExactly(0, 50, 100, 120);
        setStars("know-it-all", 200);
    }

    @Test
    void theCoreScriptRewordsTheOldPadBankBlurbAndEndTitlesButKeepsAdminChanges() throws Exception {
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
            statement.executeUpdate("UPDATE spotlight_challenges SET blurb = 'Serve each visitor the right pack. The first visitors each day are free.' WHERE id = 'pad-bank-run'");
            statement.executeUpdate("UPDATE spotlight_challenges SET blurb = 'Admin text' WHERE id = 'give-day'");
            statement.executeUpdate("UPDATE global_config SET padbank_day_title_perfect = 'Perfect day!', padbank_day_title_done = 'Shop shut'");
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));

            assertThat(challengeRepository.findById("pad-bank-run")).get().extracting("blurb").isEqualTo(PAD_BANK_BLURB);
            assertThat(challengeRepository.findById("give-day")).get().extracting("blurb").isEqualTo("Admin text");
            try (var rows = statement.executeQuery("SELECT padbank_day_title_perfect, padbank_day_title_done FROM global_config")) {
                while (rows.next()) {
                    assertThat(rows.getString(1)).isEqualTo("Perfect level!");
                    assertThat(rows.getString(2)).isEqualTo("Shop shut");
                }
            }
            statement.executeUpdate("UPDATE spotlight_challenges SET blurb = 'Travel across Lagos and give period kits where they’re needed.' WHERE id = 'give-day'");
            statement.executeUpdate("UPDATE global_config SET padbank_day_title_perfect = NULL, padbank_day_title_done = NULL");
        }
    }

    @Test
    void theCoreScriptRunLastSetsEveryGamePriceToAtMostFiveSp() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/quiz/know_yourself.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/padbank/content.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/giveday/content.sql"));
            jdbc.update("UPDATE quiz_levels SET cost = 40 WHERE number = 5");
            jdbc.update("UPDATE global_config SET quiz_hint_cost = 5, padbank_visitor_fee = 2, padbank_delivery_cost = 2");
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
        }

        assertThat(jdbc.queryForList("SELECT DISTINCT cost FROM quiz_levels WHERE number = 5", Integer.class)).containsExactly(5);
        assertThat(jdbc.queryForObject("SELECT MAX(cost) FROM quiz_levels", Integer.class)).isEqualTo(5);
        assertThat(jdbc.queryForObject("SELECT MAX(cost) FROM padbank_packs", Integer.class)).isEqualTo(5);
        assertThat(jdbc.queryForObject("SELECT MAX(cost) FROM giveday_travel", Integer.class)).isEqualTo(2);
        assertThat(jdbc.queryForObject("SELECT MAX(pass_cost) FROM giveday_levels", Integer.class)).isEqualTo(5);
        assertThat(jdbc.queryForList("SELECT DISTINCT gift_budget FROM giveday_levels", Integer.class)).containsExactly(5);
        assertThat(jdbc.queryForObject("SELECT MAX(cost) FROM giveday_products", Integer.class)).isLessThanOrEqualTo(5);
        assertThat(jdbc.queryForList("SELECT quiz_hint_cost + padbank_visitor_fee + padbank_delivery_cost FROM global_config", Integer.class))
                .allMatch(total -> total == 3);
    }

    private void setStars(String id, int stars) {
        SpotlightRank rank = rankRepository.findById(id).orElseThrow();
        rank.setMinStars(stars);
        rankRepository.save(rank);
    }
}
