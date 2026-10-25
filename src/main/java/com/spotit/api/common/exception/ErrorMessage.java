package com.spotit.api.common.exception;

public final class ErrorMessage {
    private ErrorMessage() {
    }

    public static final String INVALID_OR_USED_CODE = "Invalid or already-used code.";
    public static final String CODE_EXPIRED = "This code has expired.";
    public static final String TOO_MANY_CODE_ATTEMPTS = "Too many incorrect attempts. Please request a new code.";
    public static final String ACCOUNT_LOCKED = "Too many failed login attempts. Try again in 15 minutes, or reset your password.";
    public static final String CODE_VERIFIED = "Code verified.";
    public static final String EMAIL_ALREADY_REGISTERED = "An account with this email already exists.";
    public static final String INVALID_CREDENTIALS = "Invalid email or password.";
    public static final String EMAIL_NOT_VERIFIED = "Please verify your email to continue. We've sent you a new code.";
    public static final String INVALID_REFRESH_TOKEN = "Invalid or expired refresh token.";
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String ACCOUNT_DELETION_SCHEDULED = "Account deletion scheduled.";
    public static final String SIGNUP_NOT_FOUND = "We couldn't find that signup. Please start over.";
    public static final String OTP_NOT_VERIFIED = "Please verify your email code before creating a password.";
    public static final String PASSWORD_REUSED = "You cannot reuse any of your last 3 passwords.";

    public static final String PARTNER_INVITE_SELF = "You can't invite your own email.";
    public static final String PARTNER_INVITE_NEEDS_OWNER = "Finish setting up your own cycle before inviting a partner.";
    public static final String PARTNER_ALREADY_LINKED = "You already share with a partner. Stop sharing first.";
    public static final String PARTNER_INVITEE_TRACKS_CYCLE = "This email belongs to a Spot it account that tracks its own cycle, so it can't be linked as a partner.";
    public static final String PARTNER_INVITEE_LINKED = "This person is already linked to someone else on Spot it.";
    public static final String PARTNER_INVITE_LIMIT_REACHED = "You've sent 5 invites in the last 24 hours. Please try again later.";
    public static final String PARTNER_INVITE_NOT_FOUND = "There's no pending partner invite.";
    public static final String PARTNER_NO_INVITE_FOR_EMAIL = "We couldn't find an invite for this email. Ask your partner to invite the email you signed up with.";
    public static final String PARTNER_INVITE_CODE_INVALID = "That invite code isn't right. Check the email and try again.";
    public static final String PARTNER_INVITE_EXPIRED = "This invite has expired. Ask your partner to send a new one.";
    public static final String PARTNER_ACCOUNT_TRACKS_CYCLE = "This account tracks its own cycle, so it can't be linked as a partner. Sign up with the invited email instead.";
    public static final String PARTNER_LINK_NOT_FOUND = "You're not sharing with a partner.";
    public static final String PARTNER_NICKNAME_BLANK = "Please enter a name.";
    public static final String PARTNER_ALREADY_CHECKED_IN = "You've already checked in today. Come back tomorrow.";
    public static final String LOVE_NOT_FOUND = "That love note was not found.";
    public static final String LOVE_OPTION_EXISTS = "A love option with that id already exists.";
    public static final String LOVE_OPTION_NOT_FOUND = "Love option not found.";
    public static final String PARTNER_NOTHING_TO_SEND = "Pick something you did or write a note.";
    public static final String PARTNER_UNKNOWN_SUPPORT = "Please choose from the listed ways you supported her.";

    public static final String MONTH_OUT_OF_RANGE = "month must be between 1 and 12.";
    public static final String CYCLE_LENGTH_OUT_OF_RANGE = "cycleLength must be between 21 and 45.";
    public static final String PERIOD_LENGTH_OUT_OF_RANGE = "periodLength must be between 2 and 10.";
    public static final String REMINDER_DAYS_OUT_OF_RANGE = "Reminder days are outside the allowed range.";
    public static final String AGE_BELOW_MINIMUM = "You must be at least 13 to use Spot it.";
    public static final String EXPORT_JOB_NOT_FOUND = "Export job not found.";

    public static final String BADGE_NOT_FOUND = "Badge definition not found.";
    public static final String LEVEL_NOT_FOUND = "Level definition not found.";
    public static final String CHALLENGE_DEFINITION_NOT_FOUND = "Challenge definition not found.";
    public static final String CHALLENGE_NOT_FOUND = "Challenge not found.";
    public static final String CHALLENGE_NOT_YET_COMPLETE = "This challenge isn't complete yet.";
    public static final String CHALLENGE_ALREADY_CLAIMED = "This challenge's reward has already been claimed this week.";
    public static final String DAILY_AD_LIMIT_REACHED = "You've reached today's rewarded-ad limit.";

    public static final String PRODUCT_NOT_FOUND = "Product not found.";
    public static final String PREMIUM_REQUIRED = "This item requires Premium.";
    public static final String INSUFFICIENT_POINTS = "Not enough SpotPoints yet.";

    public static final String CONTENT_ITEM_NOT_FOUND = "Content item not found.";

    public static final String INVALID_DATE_RANGE = "'from' must be before or equal to 'to'.";
    public static final String PERIOD_START_AFTER_END = "'startDate' must be before or equal to 'endDate'.";
    public static final String PERIOD_RANGE_TOO_LONG = "Period range cannot exceed 14 days.";
    public static final String PERIOD_START_IN_FUTURE = "A period can't start in the future.";
    public static final String PERIOD_NOT_FOUND = "No logged period on that day.";
    public static final String PERIOD_DETAIL_DATE_OUT_OF_RANGE = "'detailDate' must fall within 'startDate' and 'endDate'.";

    public static final String RECEIPT_INVALID = "Receipt could not be verified.";
    public static final String ALREADY_SUBSCRIBED = "You already have an active subscription.";
    public static final String SUBSCRIPTION_NOT_FOUND = "No subscription found.";
    public static final String NO_PURCHASE_FOUND = "No previous purchase found for this account.";

    public static final String NO_SUCH_ENDPOINT = "No such endpoint.";
    public static final String INTERNAL_ERROR = "Something went wrong. Please try again.";

    public static String badgeAlreadyExists(String id) {
        return "A badge with id '" + id + "' already exists.";
    }

    public static String levelAlreadyExists(String id) {
        return "A level with id '" + id + "' already exists.";
    }

    public static String challengeAlreadyExists(String id) {
        return "A challenge with id '" + id + "' already exists.";
    }

    public static String productAlreadyExists(String id) {
        return "A product with id '" + id + "' already exists.";
    }

    public static String unlocksAtLevel(String minLevel) {
        return "Unlocks at " + minLevel + ".";
    }

    public static String invalidGoal(String goal) {
        return "'" + goal + "' is not a valid goal. Valid values: "
                + java.util.Arrays.toString(com.spotit.api.user.entity.Goal.values()) + ".";
    }
}
