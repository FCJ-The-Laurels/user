package FCJ.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Current Membership Response DTO")
public class CurrentMembershipResponse {
    @Schema(description = "Current membership level", example = "VIP", allowableValues = {"BASIC", "VIP", "PREMIUM"})
    private String membership;

    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "User's phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "User's address", example = "123 Main St, City, Country")
    private String address;
}

