package com.spotit.api.spotlight.games.quiz.service;

import com.spotit.api.spotlight.core.service.SpotlightRules;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.games.quiz.dto.QuizGroupAdmin;
import com.spotit.api.spotlight.games.quiz.dto.QuizLevelAdmin;
import com.spotit.api.spotlight.games.quiz.dto.QuizQuestionAdmin;
import com.spotit.api.spotlight.games.quiz.dto.QuizTopicAdmin;
import com.spotit.api.spotlight.games.quiz.entity.QuizGroup;
import com.spotit.api.spotlight.games.quiz.entity.QuizLevel;
import com.spotit.api.spotlight.games.quiz.entity.QuizQuestion;
import com.spotit.api.spotlight.games.quiz.entity.QuizTopic;
import com.spotit.api.spotlight.games.quiz.repository.QuizGroupRepository;
import com.spotit.api.spotlight.games.quiz.repository.QuizLevelRepository;
import com.spotit.api.spotlight.games.quiz.repository.QuizQuestionRepository;
import com.spotit.api.spotlight.games.quiz.repository.QuizTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuizAdminService {
    private static final int ICON_MAX_LENGTH = 30;
    private final QuizGroupRepository groupRepository;
    private final QuizTopicRepository topicRepository;
    private final QuizLevelRepository levelRepository;
    private final QuizQuestionRepository questionRepository;
    private final ConfigurationDomainService config;

    @Transactional(readOnly = true)
    public AdminPage<QuizGroupAdmin> groups(Integer page, Integer size) {
        return paged(groupRepository.findAllByOrderBySortOrderAsc().stream().map(QuizGroupAdmin::of).toList(), page, size);
    }

    @Transactional(readOnly = true)
    public AdminPage<QuizTopicAdmin> topics(Integer page, Integer size) {
        return paged(topicRepository.findAllByOrderBySortOrderAsc().stream().map(QuizTopicAdmin::of).toList(), page, size);
    }

    @Transactional(readOnly = true)
    public AdminPage<QuizLevelAdmin> levels(String topicId, Integer page, Integer size) {
        List<QuizLevel> levels = topicId == null ? levelRepository.findAllByOrderByTopicIdAscNumberAsc()
                : levelRepository.findByTopicIdOrderByNumberAsc(topicId);
        return paged(levels.stream().map(QuizLevelAdmin::of).toList(), page, size);
    }

    @Transactional(readOnly = true)
    public AdminPage<QuizQuestionAdmin> questions(String topicId, String status, Integer page, Integer size) {
        List<QuizQuestion> list;
        if (topicId != null && status != null) {
            list = questionRepository.findByTopicIdAndStatusOrderByLevelNumberAscIdAsc(topicId, status);
        } else if (topicId != null) {
            list = questionRepository.findByTopicIdOrderByLevelNumberAscIdAsc(topicId);
        } else if (status != null) {
            list = questionRepository.findByStatusOrderByTopicIdAscLevelNumberAscIdAsc(status);
        } else {
            list = questionRepository.findAllByOrderByTopicIdAscLevelNumberAscIdAsc();
        }
        return paged(list.stream().map(QuizQuestionAdmin::of).toList(), page, size);
    }

    private <T> AdminPage<T> paged(List<T> all, Integer page, Integer size) {
        return SpotlightRules.page(all, page, size, config.getSpotlightAdminPageSize());
    }

    @Transactional
    public QuizGroupAdmin saveGroup(String id, QuizGroupAdmin body) {
        QuizGroup group = groupRepository.findById(id).orElse(null);
        if (group == null) {
            requireAll(body.label(), body.tone());
            group = QuizGroup.builder().id(id).sortOrder(100).active(true).build();
        }
        if (body.label() != null) {
            group.setLabel(body.label().strip());
        }
        if (body.tone() != null) {
            group.setTone(body.tone());
        }
        if (body.sortOrder() != null) {
            group.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            group.setActive(body.active());
        }
        return QuizGroupAdmin.of(groupRepository.save(group));
    }

    @Transactional
    public QuizTopicAdmin saveTopic(String id, QuizTopicAdmin body) {
        QuizTopic topic = topicRepository.findById(id).orElse(null);
        if (topic == null) {
            requireAll(body.name(), body.groupId());
            topic = QuizTopic.builder().id(id).minAge(13).sortOrder(100).active(true).build();
        }
        if (body.groupId() != null) {
            if (!groupRepository.existsById(body.groupId())) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_UNKNOWN_GROUP);
            }
            topic.setGroupId(body.groupId());
        }
        if (body.name() != null) {
            topic.setName(body.name().strip());
        }
        if (body.minAge() != null) {
            topic.setMinAge(body.minAge());
        }
        if (body.sortOrder() != null) {
            topic.setSortOrder(body.sortOrder());
        }
        if (body.active() != null) {
            topic.setActive(body.active());
        }
        if (body.icon() != null) {
            String icon = body.icon().strip();
            if (icon.length() > ICON_MAX_LENGTH) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_ICON_TOO_LONG);
            }
            topic.setIcon(icon.isEmpty() ? null : icon);
        }
        return QuizTopicAdmin.of(topicRepository.save(topic));
    }

    @Transactional
    public QuizLevelAdmin saveLevel(String topicId, int number, QuizLevelAdmin body) {
        requireTopic(topicId);
        QuizLevel level = levelRepository.findByTopicIdAndNumber(topicId, number).orElse(null);
        if (level == null) {
            requireAll(body.name(), body.cost());
            level = QuizLevel.builder().topicId(topicId).number(number).active(true).build();
        }
        if (body.name() != null) {
            level.setName(body.name().strip());
        }
        if (body.cost() != null) {
            level.setCost(Math.max(0, body.cost()));
        }
        if (body.active() != null) {
            level.setActive(body.active());
        }
        if (body.lives() != null) {
            level.setLives(body.lives() > 0 ? body.lives() : null);
        }
        return QuizLevelAdmin.of(levelRepository.save(level));
    }

    @Transactional
    public QuizQuestionAdmin saveQuestion(String id, QuizQuestionAdmin body) {
        QuizQuestion question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            requireAll(body.topicId(), body.levelNumber(), body.statement(), body.answer(), body.explanation());
            question = QuizQuestion.builder().id(id).active(true).build();
        }
        boolean contentChanged = false;
        if (body.topicId() != null) {
            requireTopic(body.topicId());
            contentChanged |= !body.topicId().equals(question.getTopicId());
            question.setTopicId(body.topicId());
        }
        if (body.levelNumber() != null) {
            contentChanged |= body.levelNumber() != question.getLevelNumber();
            question.setLevelNumber(body.levelNumber());
        }
        if (body.answer() != null) {
            if (!QuizQuestion.MYTH.equals(body.answer()) && !QuizQuestion.FACT.equals(body.answer())) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_BAD_ANSWER);
            }
            contentChanged |= !body.answer().equals(question.getAnswer());
            question.setAnswer(body.answer());
        }
        contentChanged |= changed(body.statement(), question.getStatement());
        contentChanged |= changed(body.hint(), question.getHint());
        contentChanged |= changed(body.explanation(), question.getExplanation());
        contentChanged |= changed(body.funFact(), question.getFunFact());
        contentChanged |= changed(body.source(), question.getSource());
        if (body.statement() != null) {
            question.setStatement(body.statement().strip());
        }
        if (body.hint() != null) {
            question.setHint(body.hint().strip());
        }
        if (body.explanation() != null) {
            question.setExplanation(body.explanation().strip());
        }
        if (body.funFact() != null) {
            question.setFunFact(body.funFact().strip());
        }
        if (body.source() != null) {
            question.setSource(body.source().strip());
        }
        if (body.active() != null) {
            question.setActive(body.active());
        }
        if (contentChanged || question.getStatus() == null) {
            question.setStatus(QuizQuestion.DRAFT);
            question.setReviewedBy(null);
        }
        return QuizQuestionAdmin.of(questionRepository.save(question));
    }

    @Transactional
    public QuizQuestionAdmin approve(String id, String reviewedBy) {
        QuizQuestion question = questionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.QUIZ_QUESTION_NOT_FOUND));
        question.setStatus(QuizQuestion.APPROVED);
        question.setReviewedBy(reviewedBy.strip());
        return QuizQuestionAdmin.of(questionRepository.save(question));
    }

    private void requireTopic(String topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_UNKNOWN_TOPIC);
        }
    }

    private static boolean changed(String incoming, String current) {
        return incoming != null && !Objects.equals(incoming.strip(), current);
    }

    private static void requireAll(Object... values) {
        if (Stream.of(values).anyMatch(Objects::isNull)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_NEW_ITEM_INCOMPLETE);
        }
    }
}
