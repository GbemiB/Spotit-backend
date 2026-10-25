package com.spotit.api.partner.entity;

public enum ExpressionType {
    hug("expressionType.hug", "🤗", "a hug"),
    kiss("expressionType.kiss", "😘", "a kiss"),
    love("expressionType.love", "❤️", "“Love you”"),
    thinking("expressionType.thinking", "🌸", "“Thinking of you”"),
    strong("expressionType.strong", "💪", "“You’ve got this”"),
    here("expressionType.here", "🫶", "“Here for you”");

    private final String code;
    private final String emoji;
    private final String phrase;

    ExpressionType(String code, String emoji, String phrase) {
        this.code = code;
        this.emoji = emoji;
        this.phrase = phrase;
    }

    public String getCode() {
        return code;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getPhrase() {
        return phrase;
    }
}
