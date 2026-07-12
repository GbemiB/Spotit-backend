package com.spotit.api.user.entity;

/**
 * Onboarding goal options. {@code code}/{@code descriptionCode} are
 * message-bundle keys (see {@link com.spotit.api.common.i18n.EnumMessages})
 * resolved into display text for {@code GET /api/v1/onboarding/template} —
 * the mobile goals step renders from that response instead of hardcoding
 * copy, and {@link com.spotit.api.user.dto.OnboardingRequest#goal()} is
 * validated against these same constants.
 */
public enum Goal {
    track(1, "goal.track", "goal.track.description"),
    conceive(2, "goal.conceive", "goal.conceive.description"),
    avoid(3, "goal.avoid", "goal.avoid.description"),
    curious(4, "goal.curious", "goal.curious.description");

    private final Integer value;
    private final String code;
    private final String descriptionCode;

    Goal(Integer value, String code, String descriptionCode) {
        this.value = value;
        this.code = code;
        this.descriptionCode = descriptionCode;
    }

    public static Goal fromInt(Integer value) {
        return switch (value) {
            case 1 -> track;
            case 2 -> conceive;
            case 3 -> avoid;
            case 4 -> curious;
            default -> throw new IllegalArgumentException("Unknown Goal value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getDescriptionCode() {
        return descriptionCode;
    }

    public boolean isTrack() {
        return this == track;
    }

    public boolean isConceive() {
        return this == conceive;
    }

    public boolean isAvoid() {
        return this == avoid;
    }

    public boolean isCurious() {
        return this == curious;
    }
}
