package com.spotit.api.spotlight.games.giveday;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayAreaRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayLevelRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayNeedRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayPlaceRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayProductRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayTravelRepository;
import com.spotit.api.spotlight.games.giveday.service.GiveDayRules;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class GiveDayContentScriptTest {
    @Autowired DataSource dataSource;
    @Autowired GiveDayPlaceRepository placeRepository;
    @Autowired GiveDayProductRepository productRepository;
    @Autowired GiveDayNeedRepository needRepository;
    @Autowired GiveDayAreaRepository areaRepository;
    @Autowired GiveDayLevelRepository levelRepository;
    @Autowired GiveDayTravelRepository travelRepository;

    @Test
    void contentLoadsAllOfLagosAndCanBeRunTwice() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/giveday/content.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/spotlight/giveday/content.sql"));
        }

        assertThat(areaRepository.findByActiveTrueOrderBySortOrderAsc()).hasSizeGreaterThanOrEqualTo(130);
        List<GiveDayPlace> places = placeRepository.findByActiveTrueOrderBySortOrderAsc();
        assertThat(places).hasSizeGreaterThanOrEqualTo(1200);
        assertThat(productRepository.findByActiveTrueOrderBySortOrderAsc()).hasSize(10);
        assertThat(needRepository.findAll()).hasSize(places.size() * 10);
        assertThat(levelRepository.findByActiveTrueOrderByNumberAsc()).extracting("radiusKm").containsExactly(3, 8, 15, 30, 999);
        assertThat(travelRepository.findByActiveTrueOrderBySortOrderAsc()).extracting("id").containsExactly("walk", "bike", "car");
        assertThat(placeRepository.findByAreaIdAndActiveTrueOrderBySortOrderAsc("yaba")).isNotEmpty()
                .allSatisfy(p -> assertThat(GiveDayRules.decodeFacts(p.getFacts())).hasSize(4));
        assertThat(placeRepository.findByAreaIdAndActiveTrueOrderBySortOrderAsc("ikota")).hasSizeGreaterThanOrEqualTo(8)
                .allSatisfy(p -> assertThat(GiveDayRules.decodeFacts(p.getFacts())).hasSize(4));
    }
}
