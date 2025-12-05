package FCJ.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Schema(description = "User Information Creation DTO")
public class UserInfoCreation {
    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;

    @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "User address", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "Membership status", example = "false")
    private String membership;

}
