package msa.reservation.dto.request;

public record UserJoinRequest(
        String email,
        String userName,
          String password,
          String address,
          String callNumber
) {
}
