package FCJ.user.controller;

import FCJ.user.dto.UserInfoCreation;
import FCJ.user.dto.UserInfoDTO;
import FCJ.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-info")
@RequiredArgsConstructor
@Tag(name = "User Info", description = "User Information Management API")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping
    @Operation(summary = "Create new user info", description = "Creates a new user information record with all fields. User ID is extracted from AWS API Gateway header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User info created successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInfoDTO> createUserInfo(
            @Parameter(description = "User ID from AWS API Gateway", required = true)
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UserInfoCreation userInfoCreation) {
        UserInfoDTO created = userInfoService.createUserInfo(UUID.fromString(userId), userInfoCreation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/empty")
    @Operation(summary = "Create empty user info", description = "Creates an empty user information record. User ID is extracted from AWS API Gateway header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empty user info created successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInfoDTO> createEmptyUserInfo(
            @Parameter(description = "User ID from AWS API Gateway", required = true)
            @RequestHeader("X-User-Id") String userId) {
        UserInfoDTO created = userInfoService.createEmptyUserInfo(UUID.fromString(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user info by ID", description = "Retrieves user information by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info found",
                    content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User info not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInfoDTO> getUserInfoById(
            @Parameter(description = "UUID of the user info to retrieve", required = true)
            @PathVariable UUID id) {
        UserInfoDTO userInfo = userInfoService.getUserInfoById(id);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/by-user-id")
    @Operation(summary = "Get user info by User ID", description = "Retrieves user information by User ID from AWS API Gateway header")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info found",
                    content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User info not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInfoDTO> getUserInfoByUserId(
            @Parameter(description = "User ID from AWS API Gateway", required = true)
            @RequestHeader("X-User-Id") String userId) {
        UserInfoDTO userInfo = userInfoService.getUserInfoByUserId(UUID.fromString(userId));
        return ResponseEntity.ok(userInfo);
    }
    @Operation(summary = "Update user info", description = "Updates all fields of an existing user information record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info updated successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User info not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInfoDTO> updateUserInfo(
            @Parameter(description = "UUID of the user info to update", required = true)
            @PathVariable UUID id,
            @RequestBody UserInfoCreation userInfoCreation) {
        UserInfoDTO updated = userInfoService.updateUserInfo(id, userInfoCreation);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping
    @Operation(summary = "Patch user info by User ID", description = "Partially updates fields of user information using User ID from AWS API Gateway header")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info patched successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User info not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInfoDTO> patchUserInfoByUserId(
            @Parameter(description = "User ID from AWS API Gateway", required = true)
            @RequestHeader("X-User-Id") String userId,
            @RequestBody UserInfoCreation userInfoCreation) {
        UserInfoDTO patched = userInfoService.patchUserInfoByUserId(UUID.fromString(userId), userInfoCreation);
        return ResponseEntity.ok(patched);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch user info", description = "Partially updates fields of an existing user information record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User info patched successfully",
                    content = @Content(schema = @Schema(implementation = UserInfoDTO.class))),
            @ApiResponse(responseCode = "404", description = "User info not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserInfoDTO> patchUserInfo(
            @Parameter(description = "UUID of the user info to patch", required = true)
            @PathVariable UUID id,
            @RequestBody UserInfoCreation userInfoCreation) {
        UserInfoDTO patched = userInfoService.patchUserInfo(id, userInfoCreation);
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user info", description = "Deletes a user information record by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User info deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User info not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteUserInfo(
            @Parameter(description = "UUID of the user info to delete", required = true)
            @PathVariable UUID id) {
        userInfoService.deleteUserInfo(id);
        return ResponseEntity.noContent().build();
    }
}
