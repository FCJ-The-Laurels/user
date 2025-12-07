package FCJ.user.service;

import FCJ.user.dto.UserInfoCreation;
import FCJ.user.dto.UserInfoDTO;
import FCJ.user.exception.UserInfoNotFoundException;
import FCJ.user.model.UserInfo;
import FCJ.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfoDTO createUserInfo(UUID userId, UserInfoCreation userInfoCreation) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setFullName(userInfoCreation.getFullName());
        userInfo.setAvatarUrl(userInfoCreation.getAvatarUrl());
        userInfo.setPhoneNumber(userInfoCreation.getPhoneNumber());
        userInfo.setAddress(userInfoCreation.getAddress());
        // Set subscription fields
        if (userInfoCreation.getSubscriptionTier() != null) {
            userInfo.setSubscriptionTier(userInfoCreation.getSubscriptionTier());
        }
        if (userInfoCreation.getSubscriptionStatus() != null) {
            userInfo.setSubscriptionStatus(userInfoCreation.getSubscriptionStatus());
        }
        if (userInfoCreation.getSubscriptionExpiresAt() != null) {
            userInfo.setSubscriptionExpiresAt(userInfoCreation.getSubscriptionExpiresAt());
        }
        if (userInfoCreation.getPaymentMethod() != null) {
            userInfo.setPaymentMethod(userInfoCreation.getPaymentMethod());
        }
        if (userInfoCreation.getLastPaymentId() != null) {
            userInfo.setLastPaymentId(userInfoCreation.getLastPaymentId());
        }
        if (userInfoCreation.getLastPaymentDate() != null) {
            userInfo.setLastPaymentDate(userInfoCreation.getLastPaymentDate());
        }
        if (userInfoCreation.getLastPaymentAmount() != null) {
            userInfo.setLastPaymentAmount(userInfoCreation.getLastPaymentAmount());
        }
        if (userInfoCreation.getNextBillingDate() != null) {
            userInfo.setNextBillingDate(userInfoCreation.getNextBillingDate());
        }
        if (userInfoCreation.getAutoRenewal() != null) {
            userInfo.setAutoRenewal(userInfoCreation.getAutoRenewal());
        }

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(savedUserInfo);
    }

    @Override
    public UserInfoDTO createEmptyUserInfo(UUID userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
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
    public UserInfoDTO getUserInfoByUserId(UUID userId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with userId: " + userId));
        return convertToDTO(userInfo);
    }

    @Override
    public UserInfoDTO updateUserInfo(UUID id, UserInfoCreation userInfoCreation) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with id: " + id));

        userInfo.setFullName(userInfoCreation.getFullName());
        userInfo.setAvatarUrl(userInfoCreation.getAvatarUrl());
        userInfo.setPhoneNumber(userInfoCreation.getPhoneNumber());
        userInfo.setAddress(userInfoCreation.getAddress());
        // Update subscription fields
        if (userInfoCreation.getSubscriptionTier() != null) {
            userInfo.setSubscriptionTier(userInfoCreation.getSubscriptionTier());
        }
        if (userInfoCreation.getSubscriptionStatus() != null) {
            userInfo.setSubscriptionStatus(userInfoCreation.getSubscriptionStatus());
        }
        if (userInfoCreation.getSubscriptionExpiresAt() != null) {
            userInfo.setSubscriptionExpiresAt(userInfoCreation.getSubscriptionExpiresAt());
        }
        if (userInfoCreation.getPaymentMethod() != null) {
            userInfo.setPaymentMethod(userInfoCreation.getPaymentMethod());
        }
        if (userInfoCreation.getLastPaymentId() != null) {
            userInfo.setLastPaymentId(userInfoCreation.getLastPaymentId());
        }
        if (userInfoCreation.getLastPaymentDate() != null) {
            userInfo.setLastPaymentDate(userInfoCreation.getLastPaymentDate());
        }
        if (userInfoCreation.getLastPaymentAmount() != null) {
            userInfo.setLastPaymentAmount(userInfoCreation.getLastPaymentAmount());
        }
        if (userInfoCreation.getNextBillingDate() != null) {
            userInfo.setNextBillingDate(userInfoCreation.getNextBillingDate());
        }
        if (userInfoCreation.getAutoRenewal() != null) {
            userInfo.setAutoRenewal(userInfoCreation.getAutoRenewal());
        }

        UserInfo updatedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(updatedUserInfo);
    }

    @Override
    public UserInfoDTO patchUserInfo(UUID id, UserInfoCreation userInfoCreation) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with id: " + id));

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
        // Patch subscription fields
        if (userInfoCreation.getSubscriptionTier() != null) {
            userInfo.setSubscriptionTier(userInfoCreation.getSubscriptionTier());
        }
        if (userInfoCreation.getSubscriptionStatus() != null) {
            userInfo.setSubscriptionStatus(userInfoCreation.getSubscriptionStatus());
        }
        if (userInfoCreation.getSubscriptionExpiresAt() != null) {
            userInfo.setSubscriptionExpiresAt(userInfoCreation.getSubscriptionExpiresAt());
        }
        if (userInfoCreation.getPaymentMethod() != null) {
            userInfo.setPaymentMethod(userInfoCreation.getPaymentMethod());
        }
        if (userInfoCreation.getLastPaymentId() != null) {
            userInfo.setLastPaymentId(userInfoCreation.getLastPaymentId());
        }
        if (userInfoCreation.getLastPaymentDate() != null) {
            userInfo.setLastPaymentDate(userInfoCreation.getLastPaymentDate());
        }
        if (userInfoCreation.getLastPaymentAmount() != null) {
            userInfo.setLastPaymentAmount(userInfoCreation.getLastPaymentAmount());
        }
        if (userInfoCreation.getNextBillingDate() != null) {
            userInfo.setNextBillingDate(userInfoCreation.getNextBillingDate());
        }
        if (userInfoCreation.getAutoRenewal() != null) {
            userInfo.setAutoRenewal(userInfoCreation.getAutoRenewal());
        }

        UserInfo patchedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(patchedUserInfo);
    }

    @Override
    public UserInfoDTO patchUserInfoByUserId(UUID userId, UserInfoCreation userInfoCreation) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with userId: " + userId));

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
        // Patch subscription fields
        if (userInfoCreation.getSubscriptionTier() != null) {
            userInfo.setSubscriptionTier(userInfoCreation.getSubscriptionTier());
        }
        if (userInfoCreation.getSubscriptionStatus() != null) {
            userInfo.setSubscriptionStatus(userInfoCreation.getSubscriptionStatus());
        }
        if (userInfoCreation.getSubscriptionExpiresAt() != null) {
            userInfo.setSubscriptionExpiresAt(userInfoCreation.getSubscriptionExpiresAt());
        }
        if (userInfoCreation.getPaymentMethod() != null) {
            userInfo.setPaymentMethod(userInfoCreation.getPaymentMethod());
        }
        if (userInfoCreation.getLastPaymentId() != null) {
            userInfo.setLastPaymentId(userInfoCreation.getLastPaymentId());
        }
        if (userInfoCreation.getLastPaymentDate() != null) {
            userInfo.setLastPaymentDate(userInfoCreation.getLastPaymentDate());
        }
        if (userInfoCreation.getLastPaymentAmount() != null) {
            userInfo.setLastPaymentAmount(userInfoCreation.getLastPaymentAmount());
        }
        if (userInfoCreation.getNextBillingDate() != null) {
            userInfo.setNextBillingDate(userInfoCreation.getNextBillingDate());
        }
        if (userInfoCreation.getAutoRenewal() != null) {
            userInfo.setAutoRenewal(userInfoCreation.getAutoRenewal());
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
        // Map subscription fields
        dto.setSubscriptionTier(userInfo.getSubscriptionTier());
        dto.setSubscriptionStatus(userInfo.getSubscriptionStatus());
        dto.setSubscriptionExpiresAt(userInfo.getSubscriptionExpiresAt());
        dto.setPaymentMethod(userInfo.getPaymentMethod());
        dto.setLastPaymentId(userInfo.getLastPaymentId());
        dto.setLastPaymentDate(userInfo.getLastPaymentDate());
        dto.setLastPaymentAmount(userInfo.getLastPaymentAmount());
        dto.setNextBillingDate(userInfo.getNextBillingDate());
        dto.setAutoRenewal(userInfo.getAutoRenewal());
        return dto;
    }
}
