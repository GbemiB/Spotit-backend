package com.spotit.api.partner.service;

import com.spotit.api.partner.entity.LoveOption;
import com.spotit.api.partner.repository.LoveOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoveOptionSeeder {
    private final LoveOptionRepository loveOptionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {
        List<LoveOption> missing = defaults().stream()
                .filter(option -> loveOptionRepository.findByKindOrderBySortOrderAsc(option.getKind()).isEmpty())
                .toList();
        if (missing.isEmpty()) {
            return;
        }
        loveOptionRepository.saveAll(missing);
        log.info("Seeded {} default love option(s).", missing.size());
    }

    static List<LoveOption> defaults() {
        List<LoveOption> all = new ArrayList<>();
        all.add(promise("checked_in", 1, "Check on her", "💬", 1));
        all.add(promise("listened", 6, "Listen to her", "👂", 2));
        all.add(promise("snacks", 2, "Tea & treats", "☕", 3));
        all.add(promise("massage", 3, "Back rub", "💆", 4));
        all.add(promise("chores", 4, "Do the chores", "🧺", 5));
        all.add(promise("quiet_time", 5, "Us time", "🌙", 6));
        all.add(expression("hug", "Hug", "🤗", "a hug", 1));
        all.add(expression("kiss", "Kiss", "😘", "a kiss", 2));
        all.add(expression("love", "Love you", "❤️", "“Love you”", 3));
        all.add(expression("thinking", "Thinking of you", "🌸", "“Thinking of you”", 4));
        all.add(expression("strong", "You’ve got this", "💪", "“You’ve got this”", 5));
        all.add(expression("here", "Here for you", "🫶", "“Here for you”", 6));
        all.add(simple(LoveOption.STARTER, "starter_proud", "Proud of you", null, 1));
        all.add(simple(LoveOption.STARTER, "starter_thinking", "Thinking of you", null, 2));
        all.add(simple(LoveOption.STARTER, "starter_got_this", "You’ve got this", null, 3));
        all.add(simple(LoveOption.STARTER, "starter_here", "I’m here for you", null, 4));
        all.add(simple(LoveOption.REPLY, "reply_thank_you", "Thank you, love ♡", null, 1));
        all.add(simple(LoveOption.REPLY, "reply_made_my_day", "That made my day 🌸", null, 2));
        all.add(simple(LoveOption.REPLY, "reply_cared_for", "I feel so cared for 🥹", null, 3));
        all.add(simple(LoveOption.REPLY, "reply_favourite", "You’re my favourite ❤️", null, 4));
        all.add(simple(LoveOption.CATEGORY, "promise", "Make a promise", "Do something for her today", 1));
        all.add(simple(LoveOption.CATEGORY, "text", "Send a text", "Say something sweet", 2));
        all.add(simple(LoveOption.CATEGORY, "expression", "Send an expression", "A hug, a kiss, a smile", 3));
        return all;
    }

    private static LoveOption promise(String id, int value, String label, String emoji, int order) {
        return LoveOption.builder().id(id).kind(LoveOption.PROMISE).label(label).emoji(emoji).storedValue(value).sortOrder(order).active(true)
                .build();
    }

    private static LoveOption expression(String id, String label, String emoji, String phrase, int order) {
        return LoveOption.builder().id(id).kind(LoveOption.EXPRESSION).label(label).emoji(emoji).phrase(phrase).sortOrder(order).active(true)
                .build();
    }

    private static LoveOption simple(String kind, String id, String label, String sub, int order) {
        return LoveOption.builder().id(id).kind(kind).label(label).sub(sub).sortOrder(order).active(true).build();
    }
}
