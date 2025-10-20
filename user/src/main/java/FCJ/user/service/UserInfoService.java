package FCJ.user.service;

import FCJ.user.dto.EmptyUserInfoCreation;
import FCJ.user.dto.UserInfoCreation;
import FCJ.user.dto.UserInfoDTO;

import java.util.List;
import java.util.UUID;

public interface UserInfoService {
    UserInfoDTO createUserInfo(UserInfoCreation userInfoCreation);
    UserInfoDTO createEmptyUserInfo(EmptyUserInfoCreation emptyUserInfoCreation);
    UserInfoDTO getUserInfoById(UUID id);
    List<UserInfoDTO> getAllUserInfo();
    UserInfoDTO updateUserInfo(UUID id, UserInfoCreation userInfoCreation);
    UserInfoDTO patchUserInfo(UUID id, UserInfoCreation userInfoCreation);
    void deleteUserInfo(UUID id);
}
