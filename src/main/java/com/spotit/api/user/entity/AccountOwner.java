package com.spotit.api.user.entity;

public enum AccountOwner {
    owner(1, "accountType.owner"),
    partner(2, "accountType.partner");

    private final Integer value;
    private final String code;

    AccountOwner(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static AccountOwner fromInt(Integer value) {
        return switch (value) {
            case 1 -> owner;
            case 2 -> partner;
            default -> throw new IllegalArgumentException("Unknown AccountOwner value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isOwner() {
        return this == owner;
    }

    public boolean isPartner() {
        return this == partner;
    }
}
