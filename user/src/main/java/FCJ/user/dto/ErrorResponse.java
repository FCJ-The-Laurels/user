package FCJ.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Error Response DTO")
public class ErrorResponse {
    @Schema(description = "Timestamp when the error occurred", example = "2025-10-19T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error type", example = "Not Found")
    private String error;

    @Schema(description = "Detailed error message", example = "UserInfo not found with id: 123e4567-e89b-12d3-a456-426614174000")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/api/user-info/123e4567-e89b-12d3-a456-426614174000")
    private String path;
}
