package com.spotit.api.spotlight.giveday.controller;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.giveday.dto.GiveDayAreaAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayAreaResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayAreaView;
import com.spotit.api.spotlight.giveday.dto.GiveDayFact;
import com.spotit.api.spotlight.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayHomeRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayHomeResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayLevelAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayLevelView;
import com.spotit.api.spotlight.giveday.dto.GiveDayNeedAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayPlaceView;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayProductView;
import com.spotit.api.spotlight.giveday.dto.GiveDayReview;
import com.spotit.api.spotlight.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelAdmin;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelView;
import com.spotit.api.spotlight.giveday.dto.GiveDayTripQuote;
import com.spotit.api.spotlight.giveday.dto.GiveDayTripsResponse;
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
    void playersStartPickAHomeTravelGiveAndFinish() throws Exception {
        MockMvc mvc = mockMvc(new GiveDayController(service));
        authenticate();
        when(service.start(USER_ID)).thenReturn(new GiveDayStartResponse(runId, 25, 0, 480, 1080, 30, 100, null, 0, 0, 0, 1,
                List.of(new GiveDayLevelView(1, "Near home", 3, 0, 0, 5)), List.of(new GiveDayTravelView("walk", "Walk", 0)),
                List.of(new GiveDayAreaView("yaba", "Yaba", "Mainland", 7600, 2600, 9, 1, false, 0)),
                List.of(new GiveDayProductView("pads", "Pads", 2, true))));
        GiveDayHomeRequest home = new GiveDayHomeRequest("yaba");
        when(service.home(USER_ID, home)).thenReturn(new GiveDayHomeResponse("yaba", 7600, 2600, List.of()));
        when(service.area(USER_ID, runId, "yaba")).thenReturn(new GiveDayAreaResponse("yaba", "Yaba", 1, false, 0, false, 0, 5,
                List.of(new GiveDayPlaceView("yaba-school", "Bright School", "School", "40 girls", "Help", "Miss Ada", "teacher", "girls", 40,
                        List.of(new GiveDayFact("girls", "pink", "40 girls")), 7650, 2600, 900, false, false, null))));
        when(service.trips(USER_ID, runId, "yaba-school")).thenReturn(new GiveDayTripsResponse("yaba-school", 480, 900, 1, true, 0, false, 0, 5,
                List.of(new GiveDayTripQuote("walk", "Walk", 0, 10, 490, true))));
        GiveDayTravelRequest travel = new GiveDayTravelRequest("yaba-school", "walk");
        when(service.travel(USER_ID, runId, travel)).thenReturn(new GiveDayTravelResponse("yaba-school", "walk", 10, 490, 7650, 2600, 0, 0, 25));
        GiveDayGiveRequest give = new GiveDayGiveRequest("yaba-school", List.of("pads"));
        when(service.give(USER_ID, runId, give)).thenReturn(new GiveDayGiveResponse("yaba-school", 2, "Lovely work", "Thanks",
                List.of(new GiveDayReview("pads", "Pads", "good", "Easy")), List.of(), 40, 2, 5, 40, 520, 2, 1));
        when(service.finish(USER_ID, runId)).thenReturn(new GiveDayFinishResponse(2, 40, 0, Map.of("yaba-school", 2)));

        mvc.perform(post("/api/v1/giveday/runs")).andExpect(status().isOk()).andExpect(jsonPath("$.data.areas[0].region").value("Mainland"));
        mvc.perform(put("/api/v1/giveday/home").contentType(MediaType.APPLICATION_JSON).content(json(home)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.home").value("yaba"));
        mvc.perform(put("/api/v1/giveday/home").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isUnprocessableEntity());
        mvc.perform(get("/api/v1/giveday/runs/" + runId + "/areas/yaba")).andExpect(jsonPath("$.data.places[0].closes").value(900));
        mvc.perform(get("/api/v1/giveday/runs/" + runId + "/trips/yaba-school")).andExpect(jsonPath("$.data.quotes[0].arrive").value(490));
        mvc.perform(post("/api/v1/giveday/runs/" + runId + "/travel").contentType(MediaType.APPLICATION_JSON).content(json(travel)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.clock").value(490));
        mvc.perform(post("/api/v1/giveday/runs/" + runId + "/give").contentType(MediaType.APPLICATION_JSON).content(json(give)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.title").value("Lovely work"));
        mvc.perform(post("/api/v1/giveday/runs/" + runId + "/give").contentType(MediaType.APPLICATION_JSON)
                .content("{\"placeId\":\"yaba-school\",\"products\":[]}")).andExpect(status().isUnprocessableEntity());
        mvc.perform(post("/api/v1/giveday/runs/" + runId + "/finish")).andExpect(status().isOk()).andExpect(jsonPath("$.data.places.yaba-school").value(2));
    }

    @Test
    void adminsManageTheMapAndItsRules() throws Exception {
        MockMvc mvc = mockMvc(new GiveDayConfigController(adminService));
        GiveDayAreaAdmin area = new GiveDayAreaAdmin("yaba", "Yaba", "Mainland", 7600, 2600, 1, true);
        GiveDayPlaceAdmin place = new GiveDayPlaceAdmin("yaba-school", "yaba", "Bright School", "School", "40 girls", null, "Miss Ada", "teacher", "girls",
                40, null, List.of(), 7650, 2600, 900, false, 13, 1, true);
        GiveDayProductAdmin product = new GiveDayProductAdmin("pads", "Pads", 2, true, 1, true);
        GiveDayLevelAdmin level = new GiveDayLevelAdmin(3, "Across the city", 15, 10, 10, 10, true);
        GiveDayTravelAdmin bike = new GiveDayTravelAdmin("bike", "Bike", 3, 25, 2, true);
        GiveDayNeedAdmin need = new GiveDayNeedAdmin("yaba-school", "pads", "good", "Easy");
        when(adminService.areas(null, null)).thenReturn(new AdminPage<>(List.of(area), 0, 50, 1));
        when(adminService.saveArea(eq("yaba"), any())).thenReturn(area);
        when(adminService.places(null, null)).thenReturn(new AdminPage<>(List.of(place), 0, 50, 1));
        when(adminService.savePlace(eq("yaba-school"), any())).thenReturn(place);
        when(adminService.products(null, null)).thenReturn(new AdminPage<>(List.of(product), 0, 50, 1));
        when(adminService.saveProduct(eq("pads"), any())).thenReturn(product);
        when(adminService.levels(null, null)).thenReturn(new AdminPage<>(List.of(level), 0, 50, 1));
        when(adminService.saveLevel(eq(3), any())).thenReturn(level);
        when(adminService.travel(null, null)).thenReturn(new AdminPage<>(List.of(bike), 0, 50, 1));
        when(adminService.saveTravel(eq("bike"), any())).thenReturn(bike);
        when(adminService.needs("yaba-school", null, null)).thenReturn(new AdminPage<>(List.of(need), 0, 50, 1));
        when(adminService.saveNeed(eq("yaba-school"), eq("pads"), any())).thenReturn(need);

        mvc.perform(get("/api/v1/config/giveday/areas")).andExpect(jsonPath("$.data.items[0].region").value("Mainland"));
        mvc.perform(put("/api/v1/config/giveday/areas/yaba").contentType(MediaType.APPLICATION_JSON).content(json(area))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/giveday/places")).andExpect(jsonPath("$.data.items[0].areaId").value("yaba"));
        mvc.perform(put("/api/v1/config/giveday/places/yaba-school").contentType(MediaType.APPLICATION_JSON).content(json(place))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/giveday/products")).andExpect(jsonPath("$.data.items[0].cost").value(2));
        mvc.perform(put("/api/v1/config/giveday/products/pads").contentType(MediaType.APPLICATION_JSON).content(json(product))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/giveday/levels")).andExpect(jsonPath("$.data.items[0].radiusKm").value(15));
        mvc.perform(put("/api/v1/config/giveday/levels/3").contentType(MediaType.APPLICATION_JSON).content(json(level))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/giveday/travel")).andExpect(jsonPath("$.data.items[0].unitsPerMinute").value(25));
        mvc.perform(put("/api/v1/config/giveday/travel/bike").contentType(MediaType.APPLICATION_JSON).content(json(bike))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/giveday/needs").param("placeId", "yaba-school")).andExpect(jsonPath("$.data.items[0].rating").value("good"));
        mvc.perform(put("/api/v1/config/giveday/needs/yaba-school/pads").contentType(MediaType.APPLICATION_JSON).content(json(need)))
                .andExpect(status().isOk());
    }
}
