package com.spotit.api.spotlight.quiz.dto;

import java.util.List;

public record GroupView(String id, String label, String tone, List<TopicCard> topics) {
}
