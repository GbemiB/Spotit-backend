package com.spotit.api.spotlight.games.padbank.dto;

import java.util.List;

public record PadBankPackView(String id, String name, int cost, int stock, String color, List<String> contents) {
}
