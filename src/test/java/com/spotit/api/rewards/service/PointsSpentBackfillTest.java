package com.spotit.api.rewards.service;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PointsSpentBackfillTest {
    @Test
    void fillsOnlyUsersWithoutATotalYet() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.update(PointsSpentBackfill.SQL)).thenReturn(2).thenReturn(0);
        PointsSpentBackfill backfill = new PointsSpentBackfill(jdbc);

        backfill.run(null);
        backfill.run(null);

        verify(jdbc, org.mockito.Mockito.times(2)).update(PointsSpentBackfill.SQL);
    }
}
