package msa.reservation.dto.response;

public record UserLoginResponse(
        String accessToken,
        String refreshToken
){
}
