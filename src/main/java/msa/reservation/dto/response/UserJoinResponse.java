package msa.reservation.dto.response;

import msa.reservation.domain.constant.UserRole;
import msa.reservation.domain.entity.User;
import msa.reservation.dto.UserDto;

public record UserJoinResponse (
        Long id,
        String userName,
        String email
){

    public static UserJoinResponse from(UserDto userDto){
        return UserJoinResponse.of(
                userDto.id(),
                userDto.userName(),
                userDto.email()
        );
    }

    public static UserJoinResponse of(Long id, String userName, String email){
        return new UserJoinResponse(id,userName,email);
    }
}
