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
@Schema(description = "Membership Update Request DTO")
public class MembershipUpdateRequest {
    @Schema(description = "New membership type", example = "VIP", allowableValues = {"BASIC", "VIP", "PREMIUM"})
    private String membership;

    @Schema(description = "MoMo transaction ID for idempotency", example = "txn_abc123xyz", required = true)
    private String momoTransId;
}

