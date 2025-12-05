package FCJ.user.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Builder.Default
    private String membership = "BASIC";
    @Column(unique = true, name="momoTransId")
    private String momoTransId;
    //we will have BASIC, VIP, PREMIUM
}
