package com.spotit.api.common.web;

import com.spotit.api.common.dto.ApiErrorBody;
import com.spotit.api.common.dto.ApiResponse;
import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.spotit.api")
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return !ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                   @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        int status = currentStatus(response);

        if (body instanceof ApiErrorBody error) {
            return ApiResponse.of(status, error.message(), new ErrorDetail(error.errorCode(), error.otpId(), error.expiresInSeconds()));
        }
        if (body instanceof MessageResponse msg) {
            return ApiResponse.of(status, msg.message(), null);
        }
        return ApiResponse.of(status, defaultMessageFor(status), body);
    }

    private int currentStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            return servletResponse.getServletResponse().getStatus();
        }
        return 200;
    }

    private String defaultMessageFor(int status) {
        return switch (status) {
            case 201 -> "Created";
            case 204 -> "No Content";
            default -> "OK";
        };
    }
}
