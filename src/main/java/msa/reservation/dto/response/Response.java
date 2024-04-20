package msa.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 상태 결과값을 반환해주는 공간

@Getter
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T result;

    public static <T> Response<T> error(String errorCode){
        return new Response<>(errorCode, null);
    }

    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS",result);
    }

}
