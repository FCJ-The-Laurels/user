package FCJ.user.service;

import FCJ.user.dto.CurrentMembershipResponse;
import FCJ.user.dto.MembershipUpdateRequest;
import FCJ.user.dto.TransactionCheckResponse;
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

    @Override
    public UserInfoDTO updateMembership(UUID userId, MembershipUpdateRequest request) {
        // Check if transaction has already been processed (idempotency)
        if (request.getMomoTransId() != null && !request.getMomoTransId().isEmpty()) {
            userInfoRepository.findByMomoTransId(request.getMomoTransId())
                    .ifPresent(existing -> {
                        throw new RuntimeException("Transaction already processed with ID: " + request.getMomoTransId());
                    });
        }

        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with userId: " + userId));

        userInfo.setMembership(request.getMembership());
        userInfo.setMomoTransId(request.getMomoTransId());

        UserInfo updatedUserInfo = userInfoRepository.save(userInfo);
        return convertToDTO(updatedUserInfo);
    }

    @Override
    public TransactionCheckResponse checkTransaction(String momoTransId) {
        TransactionCheckResponse response = new TransactionCheckResponse();
        response.setMomoTransId(momoTransId);

        userInfoRepository.findByMomoTransId(momoTransId)
                .ifPresentOrElse(
                        userInfo -> {
                            response.setProcessed(true);
                            response.setMembership(userInfo.getMembership());
                            response.setMessage("Transaction already processed");
                        },
                        () -> {
                            response.setProcessed(false);
                            response.setMessage("Transaction not found or pending");
                        }
                );

        return response;
    }

    @Override
    public CurrentMembershipResponse getCurrentMembership(UUID userId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserInfoNotFoundException("UserInfo not found with userId: " + userId));

        CurrentMembershipResponse response = new CurrentMembershipResponse();
        response.setMembership(userInfo.getMembership());
        response.setFullName(userInfo.getFullName());
        response.setPhoneNumber(userInfo.getPhoneNumber());
        response.setAddress(userInfo.getAddress());

        return response;
    }

    private UserInfoDTO convertToDTO(UserInfo userInfo) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(userInfo.getId());
        dto.setUserId(userInfo.getUserId());
        dto.setFullName(userInfo.getFullName());
        dto.setAvatarUrl(userInfo.getAvatarUrl());
        dto.setPhoneNumber(userInfo.getPhoneNumber());
        dto.setAddress(userInfo.getAddress());
        dto.setMembership(userInfo.getMembership());
        return dto;
    }
}
