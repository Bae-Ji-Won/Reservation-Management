package msa.reservation.dto;

import msa.reservation.domain.constant.UserRole;
import msa.reservation.domain.entity.User;
import msa.reservation.dto.request.UserJoinRequest;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String userName,
        String password,
        String email,
        String address,
        String callNumber,
        UserRole role,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String modifiedBy
)
{

    public static UserDto of(Long userId, String userName, String password, String email, String address, String callNumber, UserRole role){
        return new UserDto(userId,userName,password,email,address,callNumber,role,null,userName,null,userName);
    }

    public static UserDto of(Long userId, String userName, String password, String email, String address, String callNumber, UserRole role,LocalDateTime createdAt, LocalDateTime updatedAt){
        return new UserDto(userId,userName,password,email,address,callNumber,role,createdAt,userName,updatedAt,userName);
    }


    // entity -> dto
    public static UserDto from(User user){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getAddress(),
                user.getCallNumber(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUsername(),
                user.getUpdatedAt(),
                user.getUsername()
        );
    }

    public User toEntity(){
        return User.of(userName,password, email,address,callNumber);
    }
}
