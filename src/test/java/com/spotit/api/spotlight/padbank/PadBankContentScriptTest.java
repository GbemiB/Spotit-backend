package com.spotit.api.spotlight.padbank;

import com.spotit.api.spotlight.padbank.repository.PadBankPackRepository;
import com.spotit.api.spotlight.padbank.repository.PadBankVisitorRepository;
import com.spotit.api.spotlight.padbank.service.PadBankRules;
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
class PadBankContentScriptTest {
    @Autowired DataSource dataSource;
    @Autowired PadBankPackRepository packRepository;
    @Autowired PadBankVisitorRepository visitorRepository;

    @Test
    void dayOneLoadsFourPacksAndThreeVisitorsAndCanBeRunTwice() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/padbank/day1.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/padbank/day1.sql"));
        }

        assertThat(packRepository.findByActiveTrueOrderBySortOrderAsc()).extracting("id").containsExactly("starter", "school", "comfort", "reusable");
        assertThat(visitorRepository.findByDayAndActiveTrueOrderBySortOrderAsc(1)).extracting("pack", "route")
                .containsExactly(org.assertj.core.groups.Tuple.tuple("starter", "pickup"), org.assertj.core.groups.Tuple.tuple("comfort", "delivery"),
                        org.assertj.core.groups.Tuple.tuple("school", "pickup"));
        assertThat(PadBankRules.decodeClues(visitorRepository.findById("day1-woman").orElseThrow().getClues())).hasSize(3);
        assertThat(PadBankRules.decodeMap(visitorRepository.findById("day1-girl").orElseThrow().getWrongWhy())).containsOnlyKeys("reusable", "comfort", "school");
    }
}
