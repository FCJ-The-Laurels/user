package FCJ.user.repository;

import FCJ.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {
    Optional<UserInfo> findById(UUID id);
    Optional<UserInfo> findByFullNameIsContainingIgnoreCase(String fullName);

}
