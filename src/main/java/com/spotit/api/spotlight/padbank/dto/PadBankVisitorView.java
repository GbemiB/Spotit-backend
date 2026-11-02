package com.spotit.api.spotlight.padbank.dto;

import java.util.List;

public record PadBankVisitorView(String id, String who, String look, List<PadBankClue> clues) {
}
