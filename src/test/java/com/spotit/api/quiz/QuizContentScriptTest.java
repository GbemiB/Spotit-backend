package com.spotit.api.quiz;

import com.spotit.api.quiz.entity.QuizQuestion;
import com.spotit.api.quiz.repository.QuizGroupRepository;
import com.spotit.api.quiz.repository.QuizLevelRepository;
import com.spotit.api.quiz.repository.QuizQuestionRepository;
import com.spotit.api.quiz.repository.QuizRankRepository;
import com.spotit.api.quiz.repository.QuizChallengeRepository;
import com.spotit.api.quiz.repository.QuizTopicRepository;
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
class QuizContentScriptTest {
    @Autowired DataSource dataSource;
    @Autowired QuizGroupRepository groupRepository;
    @Autowired QuizTopicRepository topicRepository;
    @Autowired QuizLevelRepository levelRepository;
    @Autowired QuizQuestionRepository questionRepository;
    @Autowired QuizRankRepository rankRepository;
    @Autowired QuizChallengeRepository challengeRepository;

    @Test
    void theScriptLoadsTenTopicsFiveLevelsAndFiveHundredApprovedQuestionsAndCanBeRunTwice() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/quiz/know_yourself.sql"));
            ScriptUtils.executeSqlScript(connection, new FileSystemResource("scripts/quiz/know_yourself.sql"));
        }

        assertThat(groupRepository.count()).isEqualTo(7);
        assertThat(challengeRepository.findByActiveTrueOrderBySortOrderAsc()).extracting("id").containsExactly("know-yourself", "pad-factory",
                "pad-bank-run", "give-day", "grow-a-maker", "clinic-shift");
        assertThat(rankRepository.findByActiveTrueOrderByMinStarsAsc()).extracting("minStars").containsExactly(0, 15, 45, 90);
        assertThat(topicRepository.count()).isEqualTo(31);
        assertThat(topicRepository.findByActiveTrueOrderBySortOrderAsc()).hasSize(10)
                .extracting("name").contains("Inside My Cycle", "Sleep, Cycle, Repeat");
        assertThat(topicRepository.findByActiveTrueOrderBySortOrderAsc()).extracting("icon")
                .containsExactly("cycle", "myth", "sprout", "bottle", "molecule", "mirror", "mood", "moon", "box", "bowl");
        assertThat(levelRepository.count()).isEqualTo(50);
        assertThat(levelRepository.findByTopicIdAndNumber("pcos", 4)).get().extracting("cost", "lives").containsExactly(20, 2);
        assertThat(levelRepository.findByTopicIdAndNumber("pcos", 5)).get().extracting("cost", "lives").containsExactly(40, 1);
        assertThat(levelRepository.findByTopicIdAndNumber("pcos", 1)).get().extracting("lives").isNull();
        assertThat(questionRepository.count()).isEqualTo(500);
        assertThat(questionRepository.findByStatusOrderByTopicIdAscLevelNumberAscIdAsc(QuizQuestion.APPROVED)).hasSize(500);
        assertThat(questionRepository.countReady()).hasSize(50);
    }
}
