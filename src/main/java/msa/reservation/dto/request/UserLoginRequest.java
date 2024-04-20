package msa.reservation.dto.request;

public record UserLoginRequest(
        String email,
        String password
) {

}
