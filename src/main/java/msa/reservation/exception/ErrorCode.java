package msa.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"User Not Found"),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User name is duplicated"),

    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"Email Not Found"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT,"Email is duplicated"),

    FORM_WRONG(HttpStatus.BAD_REQUEST, "Join Form is Wrong"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Password is invalid"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is valid"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    private HttpStatus status;
    private String message;
}
