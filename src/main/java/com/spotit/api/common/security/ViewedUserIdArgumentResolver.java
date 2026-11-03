package com.spotit.api.common.security;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ViewedUserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final PartnerLinkRepository partnerLinkRepository;

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ViewedUserId.class)
                && parameter.getParameterType().equals(UUID.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                   ModelAndViewContainer mavContainer,
                                   @NonNull NativeWebRequest webRequest,
                                   WebDataBinderFactory binderFactory) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser securityUser)) {
            throw new IllegalStateException("No authenticated SecurityUser found in SecurityContext");
        }
        if (!securityUser.partner()) {
            return securityUser.userId();
        }
        return partnerLinkRepository.findFirstByPartnerIdAndStatus(securityUser.userId(), PartnerLinkStatus.active)
                .map(PartnerLink::getOwnerId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PARTNER_LINK_NOT_FOUND));
    }
}
