package FCJ.user.service;

import FCJ.user.dto.EmptyUserInfoCreation;
import FCJ.user.dto.UserInfoCreation;
import FCJ.user.dto.UserInfoDTO;
import FCJ.user.exception.UserInfoNotFoundException;
import FCJ.user.model.UserInfo;
import FCJ.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfoDTO createUserInfo(UserInfoCreation userInfoCreation) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userInfoCreation.getUserId());
        userInfo.setFullName(userInfoCreation.getFullName());
        userInfo.setAvatarUrl(userInfoCreation.getAvatarUrl());
        userInfo.setPhoneNumber(userInfoCreation.getPhoneNumber());
        userInfo.setAddress(userInfoCreation.getAddress());

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(savedUserInfo);
    }

    @Override
    public UserInfoDTO createEmptyUserInfo(EmptyUserInfoCreation emptyUserInfoCreation) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(emptyUserInfoCreation.getUserId());
        // All other fields (fullName, avatarUrl, phoneNumber, address) remain null

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(savedUserInfo);
    }

    @Override
    public UserInfoDTO getUserInfoById(UUID id) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with id: " + id));
        return convertToDTO(userInfo);
    }

    @Override
    public List<UserInfoDTO> getAllUserInfo() {
        return userInfoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserInfoDTO updateUserInfo(UUID id, UserInfoCreation userInfoCreation) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with id: " + id));

        userInfo.setUserId(userInfoCreation.getUserId());
        userInfo.setFullName(userInfoCreation.getFullName());
        userInfo.setAvatarUrl(userInfoCreation.getAvatarUrl());
        userInfo.setPhoneNumber(userInfoCreation.getPhoneNumber());
        userInfo.setAddress(userInfoCreation.getAddress());

        UserInfo updatedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(updatedUserInfo);
    }

    @Override
    public UserInfoDTO patchUserInfo(UUID id, UserInfoCreation userInfoCreation) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with id: " + id));

        if (userInfoCreation.getUserId() != null) {
            userInfo.setUserId(userInfoCreation.getUserId());
        }
        if (userInfoCreation.getFullName() != null) {
            userInfo.setFullName(userInfoCreation.getFullName());
        }
        if (userInfoCreation.getAvatarUrl() != null) {
            userInfo.setAvatarUrl(userInfoCreation.getAvatarUrl());
        }
        if (userInfoCreation.getPhoneNumber() != null) {
            userInfo.setPhoneNumber(userInfoCreation.getPhoneNumber());
        }
        if (userInfoCreation.getAddress() != null) {
            userInfo.setAddress(userInfoCreation.getAddress());
        }

        UserInfo patchedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(patchedUserInfo);
    }

    @Override
    public void deleteUserInfo(UUID id) {
        if (!userInfoRepository.existsById(id)) {
            throw new UserInfoNotFoundException("UserInfo not found with id: " + id);
        }
        userInfoRepository.deleteById(id);
    }

    private UserInfoDTO convertToDTO(UserInfo userInfo) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(userInfo.getId());
        dto.setUserId(userInfo.getUserId());
        dto.setFullName(userInfo.getFullName());
        dto.setAvatarUrl(userInfo.getAvatarUrl());
        dto.setPhoneNumber(userInfo.getPhoneNumber());
        dto.setAddress(userInfo.getAddress());
        return dto;
    }
}
