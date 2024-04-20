package msa.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"User Not Found"),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User name is duplicated"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT,"Email is duplicated"),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Password is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    private HttpStatus status;
    private String message;
}
