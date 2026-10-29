package com.spotit.api.quiz.dto;

public record LevelView(int number, String name, int cost, String state, int stars, boolean ready, int lives) {
}
