package com.spotit.api.content.dto;

import com.spotit.api.content.entity.ContentReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public record ContentReferenceDto(
        @NotBlank @Size(max = 200) String label,
        @NotBlank @Size(max = 500) @Pattern(regexp = "^https://.+") String url
) {
    public static List<ContentReferenceDto> fromEntities(List<ContentReference> references) {
        return references == null ? List.of() : references.stream().map(r -> new ContentReferenceDto(r.getLabel(), r.getUrl())).toList();
    }

    public static List<ContentReference> toEntities(List<ContentReferenceDto> references) {
        return references == null ? new ArrayList<>()
                : new ArrayList<>(references.stream().map(r -> new ContentReference(r.label().trim(), r.url().trim())).toList());
    }
}
