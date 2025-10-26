package FCJ.user.service;

import FCJ.user.dto.UserInfoCreation;
import FCJ.user.dto.UserInfoDTO;

import java.util.UUID;

public interface UserInfoService {
    UserInfoDTO createUserInfo(UUID userId, UserInfoCreation userInfoCreation);
    UserInfoDTO createEmptyUserInfo(UUID userId);
    UserInfoDTO getUserInfoById(UUID id);
    UserInfoDTO updateUserInfo(UUID id, UserInfoCreation userInfoCreation);
    UserInfoDTO patchUserInfo(UUID id, UserInfoCreation userInfoCreation);
    void deleteUserInfo(UUID id);
}
