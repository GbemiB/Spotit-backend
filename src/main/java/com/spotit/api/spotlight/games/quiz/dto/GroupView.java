package com.spotit.api.spotlight.games.quiz.dto;

import java.util.List;

public record GroupView(String id, String label, String tone, List<TopicCard> topics) {
}
