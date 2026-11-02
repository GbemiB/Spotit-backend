package com.spotit.api.spotlight.padbank.controller;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.padbank.dto.PadBankClue;
import com.spotit.api.spotlight.padbank.dto.PadBankDayTitles;
import com.spotit.api.spotlight.padbank.dto.PadBankPackAdmin;
import com.spotit.api.spotlight.padbank.dto.PadBankPackView;
import com.spotit.api.spotlight.padbank.dto.PadBankServeRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankServeResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankStartRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankStartResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankVisitorAdmin;
import com.spotit.api.spotlight.padbank.dto.PadBankVisitorView;
import com.spotit.api.spotlight.padbank.service.PadBankAdminService;
import com.spotit.api.spotlight.padbank.service.PadBankService;
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
class PadBankControllerTest {
    @Mock PadBankService service;
    @Mock PadBankAdminService adminService;
    final UUID runId = UUID.fromString("11111111-2222-3333-4444-555555555555");

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void playersOpenTheBankAndServeVisitors() throws Exception {
        MockMvc mvc = mockMvc(new PadBankController(service));
        authenticate();
        when(service.start(USER_ID, new PadBankStartRequest(1))).thenReturn(new PadBankStartResponse(runId, 1, 40, 10, 100, 3, 10, 40,
                List.of(new PadBankPackView("starter", "Starter Bag", 10, 4, "pink", List.of("10 day pads"))),
                List.of(new PadBankVisitorView("v1", "Girl, 13", "girl", List.of(new PadBankClue("drop", "First period")))),
                new PadBankDayTitles("Perfect day!", "Bank closed for today", "Out of hearts")));
        when(service.serve(USER_ID, runId, new PadBankServeRequest(0, "starter", "pickup"))).thenReturn(new PadBankServeResponse(1, "great", "PERFECT!", "Tip",
                "Thanks", 3, 90, 10, 1, 1, Map.of("starter", 3), false, 0, List.of(false, false, false), false));

        mvc.perform(post("/api/v1/padbank/runs").contentType(MediaType.APPLICATION_JSON).content(json(new PadBankStartRequest(1))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.visitors[0].clues[0].icon").value("drop"))
                .andExpect(jsonPath("$.data.packs[0].contents[0]").value("10 day pads")).andExpect(jsonPath("$.data.dayTitles.done").value("Bank closed for today"));
        mvc.perform(post("/api/v1/padbank/runs/" + runId + "/serve").contentType(MediaType.APPLICATION_JSON)
                .content(json(new PadBankServeRequest(0, "starter", "pickup")))).andExpect(status().isOk()).andExpect(jsonPath("$.data.result").value("great"))
                .andExpect(jsonPath("$.data.title").value("PERFECT!"));
        mvc.perform(post("/api/v1/padbank/runs/" + runId + "/serve").contentType(MediaType.APPLICATION_JSON)
                .content("{\"round\":0,\"pack\":\"\",\"route\":\"pickup\"}")).andExpect(status().isUnprocessableEntity());
    }

    @Test
    void adminsManagePacksAndVisitors() throws Exception {
        MockMvc mvc = mockMvc(new PadBankConfigController(adminService));
        PadBankPackAdmin pack = new PadBankPackAdmin("starter", "Starter Bag", 10, 4, "pink", List.of("10 day pads"), 1, true);
        PadBankVisitorAdmin visitor = new PadBankVisitorAdmin("v1", 1, 1, "Girl, 13", "girl", List.of(new PadBankClue("drop", "First period")),
                "starter", "pickup", "Thanks", "Why", Map.of(), true);
        when(adminService.packs(null, null)).thenReturn(new AdminPage<>(List.of(pack), 0, 50, 1));
        when(adminService.savePack(eq("starter"), any())).thenReturn(pack);
        when(adminService.visitors(null, null)).thenReturn(new AdminPage<>(List.of(visitor), 0, 50, 1));
        when(adminService.saveVisitor(eq("v1"), any())).thenReturn(visitor);

        mvc.perform(get("/api/v1/config/padbank/packs")).andExpect(jsonPath("$.data.items[0].stock").value(4));
        mvc.perform(put("/api/v1/config/padbank/packs/starter").contentType(MediaType.APPLICATION_JSON).content(json(pack))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/padbank/visitors")).andExpect(jsonPath("$.data.items[0].route").value("pickup"));
        mvc.perform(put("/api/v1/config/padbank/visitors/v1").contentType(MediaType.APPLICATION_JSON).content(json(visitor))).andExpect(status().isOk());
    }
}
