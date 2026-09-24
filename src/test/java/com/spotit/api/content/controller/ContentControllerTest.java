package com.spotit.api.content.controller;

import com.spotit.api.content.dto.ContentFeedResponse;
import com.spotit.api.content.dto.ContentItemResponse;
import com.spotit.api.content.service.ContentReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContentControllerTest {
    @Mock ContentReadService contentReadService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new ContentController(contentReadService));
    }

    private ContentFeedResponse feed() {
        return new ContentFeedResponse(List.of(new ContentItemResponse("1", "Health", "Cycle 101", "Body", "lifestyle", false, null)));
    }

    @Test
    void feedPassesTheRequestedLimit() throws Exception {
        when(contentReadService.getFeed(3)).thenReturn(feed());

        mvc.perform(get("/api/v1/content/feed").param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].title").value("Cycle 101"));
    }

    @Test
    void feedWithoutALimitLetsTheServiceApplyItsDefault() throws Exception {
        when(contentReadService.getFeed(null)).thenReturn(feed());

        mvc.perform(get("/api/v1/content/feed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(1));
    }

    @Test
    void aNonNumericLimitIs400() throws Exception {
        mvc.perform(get("/api/v1/content/feed").param("limit", "ten"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.errorCode").value("bad_request"));
    }
}
