package com.spotit.api.appversion.dto;

public record AppVersionPolicyResponse(Platform android, Platform ios) {

    public record Platform(long minBuild, String storeUrl) {
    }
}
