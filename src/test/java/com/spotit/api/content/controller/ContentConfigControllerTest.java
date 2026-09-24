package com.spotit.api.content.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.dto.CreateContentItemRequest;
import com.spotit.api.content.dto.UpdateContentItemRequest;
import com.spotit.api.content.service.ContentReadService;
import com.spotit.api.content.service.ContentWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContentConfigControllerTest {
    @Mock ContentReadService contentReadService;
    @Mock ContentWriteService contentWriteService;

    MockMvc mvc;
    UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new ContentConfigController(contentReadService, contentWriteService));
    }

    private ContentItemAdminResponse item(String title) {
        return new ContentItemAdminResponse(id, "Health", title, "Body", null, "lifestyle", false, null, 1, true);
    }

    @Test
    void listReturnsAllItemsIncludingInactive() throws Exception {
        when(contentReadService.listAllForAdmin()).thenReturn(List.of(item("Cycle 101")));

        mvc.perform(get("/api/v1/config/content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Cycle 101"));
    }

    @Test
    void getReturnsOneItem() throws Exception {
        when(contentReadService.getForAdmin(id)).thenReturn(item("Cycle 101"));

        mvc.perform(get("/api/v1/config/content/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()));
    }

    @Test
    void getWithAMalformedIdIs400() throws Exception {
        mvc.perform(get("/api/v1/config/content/not-a-uuid"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contentReadService);
    }

    @Test
    void createReturns201() throws Exception {
        CreateContentItemRequest request = new CreateContentItemRequest("Health", "Cycle 101", "Body", null, "lifestyle", false, null, 1, true);
        when(contentWriteService.create(request)).thenReturn(item("Cycle 101"));

        mvc.perform(post("/api/v1/config/content").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201));
    }

    @Test
    void createRequiresABody() throws Exception {
        CreateContentItemRequest request = new CreateContentItemRequest("Health", "Cycle 101", " ", null, null, false, null, 1, true);

        mvc.perform(post("/api/v1/config/content").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(contentWriteService);
    }

    @Test
    void updateAppliesThePatch() throws Exception {
        UpdateContentItemRequest request = new UpdateContentItemRequest(null, "Renamed", null, null, null, null, null, null, null);
        when(contentWriteService.update(id, request)).thenReturn(item("Renamed"));

        mvc.perform(patch("/api/v1/config/content/{id}", id).contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Renamed"));
    }

    @Test
    void deleteRemovesTheItem() throws Exception {
        mvc.perform(delete("/api/v1/config/content/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Content item deleted."));

        verify(contentWriteService).delete(id);
    }

    @Test
    void deletingAnUnknownItemIs404() throws Exception {
        org.mockito.Mockito.doThrow(new ApiException(ErrorCode.NOT_FOUND, "Not found")).when(contentWriteService).delete(id);

        mvc.perform(delete("/api/v1/config/content/{id}", id))
                .andExpect(status().isNotFound());
    }
}
