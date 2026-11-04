package com.spotit.api.spotlight.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SpotlightPricesScriptTest {
    @Autowired DataSource dataSource;
    @Autowired JdbcTemplate jdbc;

    @Test
    void everyGamePriceIsAtMostFiveSpAfterThePriceScript() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/quiz/know_yourself.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/padbank/content.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/giveday/content.sql"));
            jdbc.update("UPDATE quiz_levels SET cost = 40 WHERE number = 5");
            jdbc.update("UPDATE global_config SET quiz_hint_cost = 5, padbank_visitor_fee = 2, padbank_delivery_cost = 2");
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/prices.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/prices.sql"));
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
}
