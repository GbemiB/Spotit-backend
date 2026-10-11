package com.spotit.api.reminder.entity;

/** One value per reminder the job can send. {@code screen} tells the app where a tap on the notification lands. */
public enum ReminderType {
    period_upcoming("Period reminder", "home"),
    period_today("Period reminder", "home"),
    ovulation_upcoming("Ovulation reminder", "calendar"),
    ovulation_today("Ovulation reminder", "calendar"),
    log_nudge("Daily log", "calendar"),
    weekly_digest("Your week", "insights");

    private final String title;
    private final String screen;

    ReminderType(String title, String screen) {
        this.title = title;
        this.screen = screen;
    }

    public String getTitle() {
        return title;
    }

    public String getScreen() {
        return screen;
    }
}
