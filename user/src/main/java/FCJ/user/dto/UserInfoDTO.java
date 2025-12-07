package FCJ.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Schema(description = "User Information Response DTO")
public class UserInfoDTO {
    @Schema(description = "Unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;

    @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "User address", example = "123 Main St, City, Country")
    private String address;


    // Subscription Management Fields
    @Schema(description = "Subscription tier level", example = "BASIC", allowableValues = {"BASIC", "PREMIUM", "VIP"})
    private String subscriptionTier;

    @Schema(description = "Current subscription status", example = "ACTIVE", allowableValues = {"ACTIVE", "GRACE", "EXPIRED", "CANCELLED"})
    private String subscriptionStatus;

    @Schema(description = "Subscription expiration timestamp", example = "2025-12-31T23:59:59Z")
    private Instant subscriptionExpiresAt;

    @Schema(description = "Payment method used", example = "MOMO", allowableValues = {"MOMO", "CREDIT_CARD", "BANK_TRANSFER"})
    private String paymentMethod;

    @Schema(description = "Last payment transaction ID", example = "PAY-2025-001")
    private String lastPaymentId;

    @Schema(description = "Last payment date", example = "2025-11-30T10:30:00Z")
    private Instant lastPaymentDate;

    @Schema(description = "Last payment amount", example = "50000.0")
    private Double lastPaymentAmount;

    @Schema(description = "Next billing date", example = "2025-12-30T23:59:59Z")
    private Instant nextBillingDate;

    @Schema(description = "Auto-renewal status", example = "true")
    private Boolean autoRenewal;
}
