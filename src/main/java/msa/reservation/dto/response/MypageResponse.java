package msa.reservation.dto.response;

import msa.reservation.dto.UserDto;

public record MypageResponse(
          String email,
          String userName,
          String address,
          String callNumber
) {


    public static MypageResponse of(String email, String userName, String address, String call_number){
        return new MypageResponse(email, userName, address, call_number);
    }
}
