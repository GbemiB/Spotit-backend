package com.spotit.api.partner.dto;

import java.time.Instant;

public record HerReply(String name, boolean heart, String note, String about, Instant repliedAt) {
}
