package com.spotit.api.device.controller;

import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.service.DeviceWriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DeviceControllerTest {
    @Mock DeviceWriteService deviceWriteService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new DeviceController(deviceWriteService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void registerStoresThePushTokenForTheCurrentUser() throws Exception {
        RegisterDeviceRequest request = new RegisterDeviceRequest("fcm:token", "android");

        mvc.perform(post("/api/v1/devices/register").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Device registered."));

        verify(deviceWriteService).register(USER_ID, request);
    }

    @Test
    void registerRejectsAnUnknownPlatform() throws Exception {
        mvc.perform(post("/api/v1/devices/register").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new RegisterDeviceRequest("fcm:token", "blackberry"))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(deviceWriteService);
    }

    @Test
    void unregisterRemovesThePushToken() throws Exception {
        mvc.perform(delete("/api/v1/devices/{pushToken}", "fcm:token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Device unregistered."));

        verify(deviceWriteService).unregister("fcm:token");
    }
}
