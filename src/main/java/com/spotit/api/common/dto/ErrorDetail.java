package com.spotit.api.common.dto;

/** Carried as the envelope's {@code data} on error responses, so the short machine-readable code survives alongside the human message. */
public record ErrorDetail(String errorCode) {
}
