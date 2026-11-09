package com.spotit.api.spotlight.games.padbank.dto;

import java.util.List;

public record PadBankVisitorView(String id, String who, String look, int size, List<PadBankClue> clues) {
}
