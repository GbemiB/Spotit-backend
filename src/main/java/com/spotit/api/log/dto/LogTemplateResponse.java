package com.spotit.api.log.dto;

import java.util.List;

public record LogTemplateResponse(List<EnumOption> flow, List<EnumOption> mood, List<EnumOption> symptoms, int basePoints) {
}
