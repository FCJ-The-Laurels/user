package FCJ.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Schema(description = "Empty User Information Creation DTO - Only requires userId")
public class EmptyUserInfoCreation {
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID userId;
}
