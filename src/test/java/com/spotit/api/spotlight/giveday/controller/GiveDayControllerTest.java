package com.spotit.api.spotlight.giveday.controller;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.giveday.dto.GiveDayFact;
import com.spotit.api.spotlight.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayNeedAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceView;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductView;
import com.spotit.api.spotlight.giveday.dto.GiveDayReview;
import com.spotit.api.spotlight.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.giveday.service.GiveDayAdminService;
import com.spotit.api.spotlight.giveday.service.GiveDayService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GiveDayControllerTest {
    @Mock GiveDayService service;
    @Mock GiveDayAdminService adminService;
    final UUID runId = UUID.fromString("11111111-2222-3333-4444-555555555555");

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void playersStartGiveAndFinish() throws Exception {
        MockMvc mvc = mockMvc(new GiveDayController(service));
        authenticate();
        when(service.start(USER_ID)).thenReturn(new GiveDayStartResponse(runId, 25, 15, 200,
                List.of(new GiveDayPlaceView("hope-house", "Hope House", "Home", "18 girls", "Welcome", "Mama", "woman", "girls", 18, List.of(6, 12, 18),
                        List.of(new GiveDayFact("girls", "pink", "18 girls")), 70, 205, false, false)),
                List.of(new GiveDayProductView("pads", "Pads", 4, true))));
        GiveDayGiveRequest give = new GiveDayGiveRequest("hope-house", 18, List.of("pads"));
        when(service.give(USER_ID, runId, give)).thenReturn(new GiveDayGiveResponse("hope-house", 2, "Lovely work", "Thanks",
                List.of(new GiveDayReview("pads", "Pads", "good", "Easy")), List.of(), 18, 72, 128, 18));
        when(service.finish(USER_ID, runId)).thenReturn(new GiveDayFinishResponse(2, 18, 128, Map.of("hope-house", 2)));

        mvc.perform(post("/api/v1/giveday/runs")).andExpect(status().isOk()).andExpect(jsonPath("$.data.places[0].counts[2]").value(18));
        mvc.perform(post("/api/v1/giveday/runs/" + runId + "/give").contentType(MediaType.APPLICATION_JSON).content(json(give)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.title").value("Lovely work"));
        mvc.perform(post("/api/v1/giveday/runs/" + runId + "/give").contentType(MediaType.APPLICATION_JSON)
                .content("{\"placeId\":\"hope-house\",\"people\":18,\"products\":[]}")).andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/api/v1/giveday/runs/" + runId + "/finish")).andExpect(status().isOk()).andExpect(jsonPath("$.data.places.hope-house").value(2));
    }

    @Test
    void adminsManagePlacesProductsAndNeeds() throws Exception {
        MockMvc mvc = mockMvc(new GiveDayConfigController(adminService));
        GiveDayPlaceAdmin place = new GiveDayPlaceAdmin("hope-house", "Hope House", "Home", "18 girls", null, "Mama", "woman", "girls", 18,
                List.of(6, 12, 18), null, List.of(), 70, 205, false, 13, 1, true);
        GiveDayProductAdmin product = new GiveDayProductAdmin("pads", "Pads", 4, true, 1, true);
        GiveDayNeedAdmin need = new GiveDayNeedAdmin("hope-house", "pads", "good", "Easy");
        when(adminService.places(null, null)).thenReturn(new AdminPage<>(List.of(place), 0, 50, 1));
        when(adminService.savePlace(eq("hope-house"), any())).thenReturn(place);
        when(adminService.products(null, null)).thenReturn(new AdminPage<>(List.of(product), 0, 50, 1));
        when(adminService.saveProduct(eq("pads"), any())).thenReturn(product);
        when(adminService.needs("hope-house", null, null)).thenReturn(new AdminPage<>(List.of(need), 0, 50, 1));
        when(adminService.saveNeed(eq("hope-house"), eq("pads"), any())).thenReturn(need);

        mvc.perform(get("/api/v1/config/giveday/places")).andExpect(jsonPath("$.data.items[0].total").value(18));
        mvc.perform(put("/api/v1/config/giveday/places/hope-house").contentType(MediaType.APPLICATION_JSON).content(json(place))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/giveday/products")).andExpect(jsonPath("$.data.items[0].cost").value(4));
        mvc.perform(put("/api/v1/config/giveday/products/pads").contentType(MediaType.APPLICATION_JSON).content(json(product))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/giveday/needs").param("placeId", "hope-house")).andExpect(jsonPath("$.data.items[0].rating").value("good"));
        mvc.perform(put("/api/v1/config/giveday/needs/hope-house/pads").contentType(MediaType.APPLICATION_JSON).content(json(need)))
                .andExpect(status().isOk());
    }
}
