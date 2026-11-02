package com.spotit.api.spotlight.core;

import org.junit.jupiter.api.Test;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.repository.SpotlightRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SpotlightCoreScriptTest {
    @Autowired DataSource dataSource;
    @Autowired SpotlightChallengeRepository challengeRepository;
    @Autowired SpotlightRankRepository rankRepository;

    @Test
    void theCoreScriptLoadsSixChallengesAndFourRanksAndCanBeRunTwice() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/core.sql"));
        }

        assertThat(challengeRepository.findByActiveTrueOrderBySortOrderAsc()).extracting("id").containsExactly("know-yourself", "pad-factory",
                "pad-bank-run", "give-day", "grow-a-maker", "clinic-shift");
        assertThat(challengeRepository.findById("pad-bank-run")).get().extracting("status").isEqualTo("live");
        assertThat(rankRepository.findByActiveTrueOrderByMinStarsAsc()).extracting("minStars").containsExactly(0, 15, 45, 90);
    }
}
