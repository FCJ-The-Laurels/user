package FCJ.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID userId;
    private String fullName;
    private String avatarUrl;
    private String phoneNumber;
    private String address;

    // Subscription Management Fields
    @Builder.Default
    private String subscriptionTier = null; // BASIC, PREMIUM, VIP
    @Builder.Default
    private String subscriptionStatus = null; // ACTIVE, GRACE, EXPIRED, CANCELLED
    private Instant subscriptionExpiresAt;
    private String paymentMethod; // MOMO, CREDIT_CARD, BANK_TRANSFER
    private String lastPaymentId;
    private Instant lastPaymentDate;
    private Double lastPaymentAmount;
    private Instant nextBillingDate;
    @Builder.Default
    private Boolean autoRenewal = false;

    // Validation: Reject invalid subscription values
    @PrePersist
    @PreUpdate
    private void validateSubscription() {
        if ("TRIAL".equalsIgnoreCase(subscriptionTier)) {
            throw new IllegalArgumentException("TRIAL tier is not allowed");
        }
        if ("TRIALING".equalsIgnoreCase(subscriptionStatus)) {
            throw new IllegalArgumentException("TRIALING status is not allowed");
        }
    }
}
