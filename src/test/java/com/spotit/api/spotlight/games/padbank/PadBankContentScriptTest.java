package com.spotit.api.spotlight.games.padbank;

import com.spotit.api.spotlight.games.padbank.entity.PadBankPack;
import com.spotit.api.spotlight.games.padbank.entity.PadBankVisitor;
import com.spotit.api.spotlight.games.padbank.repository.PadBankPackRepository;
import com.spotit.api.spotlight.games.padbank.repository.PadBankVisitorRepository;
import com.spotit.api.spotlight.games.padbank.service.PadBankRules;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PadBankContentScriptTest {
    @Autowired DataSource dataSource;
    @Autowired PadBankPackRepository packRepository;
    @Autowired PadBankVisitorRepository visitorRepository;

    @Test
    void contentLoadsEightPacksAndTwentyDaysOfVisitorsAndCanBeRunTwice() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/padbank/content.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/padbank/content.sql"));
        }

        List<PadBankPack> packs = packRepository.findByActiveTrueOrderBySortOrderAsc();
        assertThat(packs).extracting("id").containsExactly("starter", "school", "comfort", "reusable", "liners", "night", "sport", "newmum");
        assertThat(packs).extracting("fromDay").containsExactly(1, 1, 1, 1, 3, 5, 7, 9);
        assertThat(visitorRepository.activeDays()).hasSize(20);
        assertThat(visitorRepository.findAll()).hasSize(100);
        for (int day = 1; day <= 20; day++) {
            int open = day;
            Set<String> openPacks = packs.stream().filter(p -> p.getFromDay() <= open).map(PadBankPack::getId).collect(Collectors.toSet());
            List<PadBankVisitor> visitors = visitorRepository.findByDayAndActiveTrueOrderBySortOrderAsc(day);
            assertThat(visitors).hasSize(5);
            Map<String, Integer> needed = new HashMap<>();
            visitors.forEach(v -> PadBankRules.decodePacks(v.getPacks()).forEach(p -> needed.merge(p, 1, Integer::sum)));
            assertThat(openPacks).containsAll(needed.keySet());
            packs.forEach(p -> assertThat(needed.getOrDefault(p.getId(), 0)).isLessThanOrEqualTo(p.getStock()));
        }
        PadBankVisitor teens = visitorRepository.findById("d13-teens").orElseThrow();
        assertThat(PadBankRules.decodePacks(teens.getPacks())).containsExactly("sport", "sport", "comfort");
        assertThat(teens.getGroupWhy()).isNotBlank();
        assertThat(PadBankRules.decodeClues(visitorRepository.findById("day1-woman").orElseThrow().getClues())).hasSize(3);
        assertThat(PadBankRules.decodeMap(visitorRepository.findById("day1-girl").orElseThrow().getWrongWhy())).containsOnlyKeys("reusable", "comfort",
                "school");
    }
}
