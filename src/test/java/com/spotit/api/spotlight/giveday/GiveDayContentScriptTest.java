package com.spotit.api.spotlight.giveday;

import com.spotit.api.spotlight.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.giveday.service.GiveDayRules;
import org.junit.jupiter.api.Test;
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
class GiveDayContentScriptTest {
    @Autowired DataSource dataSource;
    @Autowired GiveDayPlaceRepository placeRepository;
    @Autowired GiveDayProductRepository productRepository;
    @Autowired GiveDayNeedRepository needRepository;

    @Test
    void dayOneLoadsEightPlacesTenProductsAndTheirNeedsAndCanBeRunTwice() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/giveday/day1.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/giveday/day1.sql"));
        }

        assertThat(placeRepository.findByActiveTrueOrderBySortOrderAsc()).hasSize(8);
        assertThat(productRepository.findByActiveTrueOrderBySortOrderAsc()).hasSize(10);
        assertThat(needRepository.findAll()).hasSize(80);
        assertThat(needRepository.findByPlaceId("hope-house")).hasSize(10);
        assertThat(GiveDayRules.decodeCounts(placeRepository.findById("bright-school").orElseThrow().getCounts())).containsExactly(10, 20, 40);
        assertThat(GiveDayRules.decodeFacts(placeRepository.findById("hope-camp").orElseThrow().getFacts())).hasSize(4);
        assertThat(placeRepository.findById("my-kit").orElseThrow().isForSelf()).isTrue();
    }
}
