package com.spotit.api.user.service;

import com.spotit.api.user.dto.UserResponse;
import com.spotit.api.user.entity.User;

/** Shared by UserReadServiceImpl and UserWriteServiceImpl so both sides of the CQRS split render a User identically. */
final class UserMapper {

    private UserMapper() {
    }

    static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getDob(),
                user.getGoal() == null ? null : user.getGoal().name(),
                user.getCycleLength(), user.getPeriodLength(), user.getLastPeriodDate(),
                user.getThemePref().name(), user.isOnboarded(), user.isPremium()
        );
    }
}
