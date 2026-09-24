package com.spotit.api.device.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.service.DeviceWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Devices", description = "Push-notification device token registration.")
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceWriteService deviceWriteService;

    @Operation(summary = DeviceControllerSwagger.REGISTER_SUMMARY, description = DeviceControllerSwagger.REGISTER_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = RegisterDeviceRequest.class),
            examples = @ExampleObject(value = DeviceControllerSwagger.REGISTER_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device registered.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = DeviceControllerSwagger.REGISTER_200_EXAMPLE)))
    })
    @PostMapping("/register")
    public MessageResponse register(@CurrentUserId UUID userId, @Valid @RequestBody RegisterDeviceRequest request) {
        deviceWriteService.register(userId, request);
        return new MessageResponse("Device registered.");
    }

    @Operation(summary = DeviceControllerSwagger.UNREGISTER_SUMMARY, description = DeviceControllerSwagger.UNREGISTER_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Device unregistered.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = DeviceControllerSwagger.UNREGISTER_200_EXAMPLE)))
    })
    @DeleteMapping("/{pushToken}")
    public MessageResponse unregister(@Parameter(description = "Push token to remove", example = "fcm:d7f3a1b2-token-example") @PathVariable String pushToken) {
        deviceWriteService.unregister(pushToken);
        return new MessageResponse("Device unregistered.");
    }
}
