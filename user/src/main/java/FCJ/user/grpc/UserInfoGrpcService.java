package FCJ.user.grpc;

import FCJ.user.dto.UserInfoCreation;
import FCJ.user.dto.UserInfoDTO;
import FCJ.user.exception.UserInfoNotFoundException;
import FCJ.user.service.UserInfoService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserInfoGrpcService extends UserInfoServiceGrpc.UserInfoServiceImplBase {

    private final UserInfoService userInfoService;

    @Override
    public void createUserInfo(CreateUserInfoRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        try {
            // Convert gRPC request to DTO
            UUID userId = UUID.fromString(request.getUserId());
            UserInfoCreation creation = new UserInfoCreation();
            creation.setFullName(request.getFullName());
            creation.setAvatarUrl(request.getAvatarUrl());
            creation.setPhoneNumber(request.getPhoneNumber());
            creation.setAddress(request.getAddress());

            // Call service
            UserInfoDTO result = userInfoService.createUserInfo(userId, creation);

            // Convert DTO to gRPC response
            UserInfoResponse response = convertToGrpcResponse(result);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid user ID format: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error creating user info: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void createEmptyUserInfo(CreateEmptyUserInfoRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        try {
            UUID userId = UUID.fromString(request.getUserId());
            UserInfoDTO result = userInfoService.createEmptyUserInfo(userId);
            
            UserInfoResponse response = convertToGrpcResponse(result);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid user ID format: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error creating empty user info: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void getUserInfoById(GetUserInfoByIdRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            UserInfoDTO result = userInfoService.getUserInfoById(id);
            
            UserInfoResponse response = convertToGrpcResponse(result);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserInfoNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid ID format: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error retrieving user info: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void updateUserInfo(UpdateUserInfoRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            UserInfoCreation creation = new UserInfoCreation();
            creation.setFullName(request.getFullName());
            creation.setAvatarUrl(request.getAvatarUrl());
            creation.setPhoneNumber(request.getPhoneNumber());
            creation.setAddress(request.getAddress());

            UserInfoDTO result = userInfoService.updateUserInfo(id, creation);
            
            UserInfoResponse response = convertToGrpcResponse(result);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserInfoNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid ID format: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error updating user info: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void patchUserInfo(PatchUserInfoRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            UserInfoCreation creation = new UserInfoCreation();
            
            // Only set fields that are present in the request
            if (request.hasFullName()) {
                creation.setFullName(request.getFullName());
            }
            if (request.hasAvatarUrl()) {
                creation.setAvatarUrl(request.getAvatarUrl());
            }
            if (request.hasPhoneNumber()) {
                creation.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.hasAddress()) {
                creation.setAddress(request.getAddress());
            }

            UserInfoDTO result = userInfoService.patchUserInfo(id, creation);
            
            UserInfoResponse response = convertToGrpcResponse(result);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserInfoNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid ID format: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error patching user info: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteUserInfo(DeleteUserInfoRequest request, StreamObserver<DeleteUserInfoResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            userInfoService.deleteUserInfo(id);
            
            DeleteUserInfoResponse response = DeleteUserInfoResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("User info deleted successfully")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserInfoNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid ID format: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error deleting user info: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void blogUserInfo(BlogUserInfoRequest request, StreamObserver<BlogUserInfoResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            UserInfoDTO result = userInfoService.getUserInfoById(id);
            
            BlogUserInfoResponse response = BlogUserInfoResponse.newBuilder()
                    .setId(result.getId().toString())
                    .setName(result.getFullName() != null ? result.getFullName() : "")
                    .setAvatar(result.getAvatarUrl() != null ? result.getAvatarUrl() : "")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserInfoNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid ID format: " + e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error retrieving blog user info: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    /**
     * Helper method to convert UserInfoDTO to gRPC UserInfoResponse
     */
    private UserInfoResponse convertToGrpcResponse(UserInfoDTO dto) {
        UserInfoResponse.Builder builder = UserInfoResponse.newBuilder()
                .setId(dto.getId().toString())
                .setUserId(dto.getUserId().toString());
        
        if (dto.getFullName() != null) {
            builder.setFullName(dto.getFullName());
        }
        if (dto.getAvatarUrl() != null) {
            builder.setAvatarUrl(dto.getAvatarUrl());
        }
        if (dto.getPhoneNumber() != null) {
            builder.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getAddress() != null) {
            builder.setAddress(dto.getAddress());
        }
        
        return builder.build();
    }
}

