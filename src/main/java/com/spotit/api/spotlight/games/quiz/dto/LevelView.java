package com.spotit.api.spotlight.games.quiz.dto;

public record LevelView(int number, String name, int cost, String state, int stars, boolean ready, int lives) {
}
